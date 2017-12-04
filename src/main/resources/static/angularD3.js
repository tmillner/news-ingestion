  var app = angular.module("app", []);
  app.controller("controller", function($scope, $http, $timeout) {
    $http.get("/api/search/repo/sources").then(function(response) {
      $scope.sources = response.data;
    });

    $scope.radio = {
      "option": "associated-press"
    };

    $scope.dateRangeError = false;
    $scope.serverError = false;

    var request = function() {
      $scope.fromDate = $("#from_datepicker").datepicker("getDate").toISOString().split('.')[0] + "Z";
      $scope.toDate = $("#to_datepicker").datepicker("getDate").toISOString().split('.')[0] + "Z";

      $scope.dateRangeError = false;
      $scope.serverError = false;

      $http.get("/api/search/repo/" + $scope.radio.option + "/" +
        $scope.fromDate + "/" +
        $scope.toDate).then(function(response) {
          $scope.data = response.data;
          console.log(response.data);
          return $scope.renderD3View($scope.data);
      }, function(response) {
          if (response.data.message.includes("Sorry")){
            $scope.dateRangeError = true;
          }
          if (response.data.error != null && response.data.error.includes("Internal Server Error")){
            $scope.serverError = true;
          }
      });
    };

    $scope.grabArticlesForSource = function() {
      request();
    };

    $scope.renderD3View = function(data) {
      /* process data into useable format: stores keyword in a map with an array of source data */
      var keywords = {};

      for (var i in data) {
        for (var j in data[i].keywords) {

          var keyword = data[i].keywords[j];
          var dataToPush = {
            "source": data[i].source,
            "publishedAt": data[i].publishedAt,
            "title": data[i].title,
            "url": data[i].url
          };

          if (keywords[keyword] == undefined) {
            keywords[keyword] = [dataToPush];
          } else {
            keywords[keyword].push(dataToPush);
          }
        }
      }

      var d3Valid = {
        "children": []
      };
      for (var k in keywords) {
        var toAdd = {}
        toAdd["keyword"] = k;
        toAdd["source"] = keywords[k][0]["source"];
        toAdd["data"] = [];
        for (var article in keywords[k]) {
          toAdd["data"].push(keywords[k][article]);
        }
        d3Valid["children"].push(toAdd);
      }

      /* rendering */
      var width = 700;
      var height = 700;
      d3.select("svg").remove();

      var svg = d3.select("#container").append("svg")
        .attr("width", width)
        .attr("height", height)
        .attr("font-family", "sans-serif")
        .attr("font-size", 10)
        .attr("text-anchor", "middle");

      var format = d3.format(",d");

      var color = d3.scaleOrdinal(d3.schemeCategory20c);

      var pack = d3.pack()
        .size([width, height])
        .padding(1.5);

      var root = d3.hierarchy(d3Valid)
        .sum(function(d) {
          if (d.data != undefined) {
            return d.data.length;
          } else {
            return 0;
          }
        })
        .each(function(d) {
          d.id = d.data.keyword && d.data.keyword.replace(/ /g, '');
          d.package = d.data.source;
          d.class = d.data.keyword;
          d.first_letter = d.data.keyword && d.data.keyword.charAt(0).toLowerCase();
          d.other_data = d.data.data;
        });

      var links = [];
      var node = svg.selectAll(".node")
        .data(pack(root).leaves());

      node.exit().remove();
      node.exit().selectAll("circle").remove();
      node.exit().selectAll("title").remove();
      node.exit().selectAll("clipPath").remove();
      node.exit().selectAll("text").remove();

      var group = node.enter().append("g")
        .merge(node)
        .attr("class", "node")
        .attr("transform", function(d) {
          return "translate(" + d.x + "," + d.y + ")";
        });

      group.append("circle")
        .attr("id", function(d) {
          return d.id;
        })
        .attr("r", function(d) {
          return d.r;
        })
        .style("fill", function(d) {
          return color(d.first_letter);
        });

      group.append("clipPath")
        .attr("id", function(d) {
          return "clip-" + d.id;
        })
        .append("use")
        .attr("xlink:href", function(d) {
          return "#" + d.id;
        });

      group.append("text")
        .on("mouseover", function(d) {
          var titles = [];
          for (var item in d.other_data) {
            titles.push({
              "title": d.other_data[item].title,
              "url": d.other_data[item].url
            });
          }

          var n = d3.select(".info-box-inner-data-text")
            .selectAll("p")
            .data(titles);

          n.exit().remove();

          n.enter()
            .append("p")
            .merge(n)
            .call(function(selection) {

              d3.select(".info-box-wrapper")
                .classed("hidden", false)
                .style("font-weight", "bolder")
                .style("font-size", "11px")
                .style("top", d.y + 10 + d3.select("#top-panel")._groups[0][0].offsetHeight + "px")
                .style("left", d.x + 100 + d3.select("#left-panel")._groups[0][0].offsetWidth + "px");

            })
            .text(function(d) {
              return d.title
            });

        })
        .on("click", function(d) {
          // return if item already in bookmarks
          var listItemExists = false;

          d3.selectAll("#bookshelf li").each(function(e) {
            for (var item in d.other_data) {
              if (e.title == d.other_data[item].title) {
                listItemExists = true;
                return
              }
            }
          });

          if (listItemExists == true) {
            return;
          }

          for (var item in d.other_data) {
            links.push({
              "title": d.other_data[item].title,
              "url": d.other_data[item].url,
              "source": d.other_data[item].source,
              "publishedAt": d.other_data[item].publishedAt
            });
          }

          if (d3.select("#bookshelf li").size() == 0) {
            d3.select("#expand-draw")
              .attr("src", "collapse.png");
          }

          var n = d3.select("#bookshelf")
            .selectAll("li")
            .data(links.slice(links.length - d.other_data.length));

          n.enter()
            .merge(n)
            .append("li")
            .append("a")
            .attr("href", function(d) {
              return d.url
            })
            .attr("target", "_blank")
            .text(function(d) {
              return d.title
            })
            .select(function() {
              return this.parentNode;
            })
            .append("br")
            .select(function() {
              return this.parentNode;
            })
            .append("span")
            .attr("id", "source")
            .style("font-style", "italic")
            .style("padding-right", "10em")
            .text(function(d) {
              return d.source;
            })
            .select(function() {
              return this.parentNode;
            })
            .append("span")
            .attr("id", "date")
            .style("font-size", "smaller")
            .style("position", "absolute")
            .style("right", "5em")
            .text(function(d) {
              var outputDate = d.publishedAt;
              if (outputDate != null) {
                outputDate = new Date(outputDate).toString().split(/\d\d\:/)[0].trim();
              }
              return outputDate;
            });
        })
        .on("mouseout", function(d) {
          d3.select(".info-box-wrapper")
            .classed("hidden", true)

          var n = d3.select(".info-box-inner-data-text")
            .selectAll("p");

          n.exit().remove();
        })
        .attr("clip-path", function(d) {
          return "url(#clip-" + d.id + ")";
        })
        .selectAll("tspan")
        .data(function(d) {
          return d.class.split(/(?=[^a-z-])/g);
        })
        .enter().append("tspan")
        .attr("x", 0)
        .attr("y", function(d, i, nodes) {
          return 13 + (i - nodes.length / 2 - 0.5) * 10;
        })
        .text(function(d) {
          return d;
        });

      group.append("title")
        .text(function(d) {
          return d.class + "\n" + format(d.value);
        });

      var drawer = d3.select("#expand-draw")
        .on("click", function(d) {
          var status = d3.select(this).attr("src");
          var h = "0px";
          var w = "0px";
          var button = "";
          var opacity = "";

          if (status.includes('collapse')) {
            h = "0px";
            w = "0px";
            overflow = "hidden";
            var opacity = "0";
            button = "expand.png";
          } else {
            h = "100%";
            w = "100%";
            overflow = "scroll";
            var opacity = "1";
            button = "collapse.png";
          }

          if (d3.select("#bookshelf li").size() == 0) {
            return
          }

          d3.select("#bookshelf-container")
            .transition()
            .duration(1050)
            .style("height", h)
            .style("width", w)
            .style("opacity", opacity)
            .transition()
            .delay(1060)
            .style("overflow", overflow);

          d3.select(this)
            .transition()
            .delay(1060)
            .attr("src", button);
        });
    }

    /* run on load */
    $timeout(function() {
      $scope.grabArticlesForSource();
    }, 100);
  });