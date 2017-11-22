var app=angular.module("app",[]); app.controller("controller",function($scope,$http,$timeout){$http.get("/api/search/repo/sources").then(function(response) {$scope.sources = response.data; }); $scope.radio={ "option":"associated-press" }; var request=function() { $scope.fromDate = $("#from_datepicker").datepicker("getDate").toISOString().split('.')[0] + "Z"; $scope.toDate = $("#to_datepicker").datepicker("getDate").toISOString().split('.')[0] + "Z"; $http.get("/api/search/repo/" + $scope.radio.option + "/" + $scope.fromDate + "/" + $scope.toDate).then(function(response) { $scope.data = response.data; return $scope.renderD3View($scope.data); }); }; $scope.grabArticlesForSource = function() { request(); }; $scope.renderD3View=function(t){var e={};for(var r in t)for(var a in t[r].keywords){var n=t[r].keywords[a],l={source:t[r].source,publishedAt:t[r].publishedAt,title:t[r].title,url:t[r].url};void 0==e[n]?e[n]=[l]:e[n].push(l)}var i={children:[]};for(var o in e){var s={};s.keyword=o,s.source=e[o][0].source,s.data=[];for(var d in e[o])s.data.push(e[o][d]);i.children.push(s)}d3.select("svg").remove();var c=d3.select("#container").append("svg").attr("width",700).attr("height",700).attr("font-family","sans-serif").attr("font-size",10).attr("text-anchor","middle"),p=d3.format(",d"),u=d3.scaleOrdinal(d3.schemeCategory20c),f=d3.pack().size([700,700]).padding(1.5),h=d3.hierarchy(i).sum(function(t){return void 0!=t.data?t.data.length:0}).each(function(t){t.id=t.data.keyword&&t.data.keyword.replace(/ /g,""),t.package=t.data.source,t.class=t.data.keyword,t.first_letter=t.data.keyword&&t.data.keyword.charAt(0).toLowerCase(),t.other_data=t.data.data}),v=[],x=c.selectAll(".node").data(f(h).leaves());x.exit().remove(),x.exit().selectAll("circle").remove(),x.exit().selectAll("title").remove(),x.exit().selectAll("clipPath").remove(),x.exit().selectAll("text").remove();var y=x.enter().append("g").merge(x).attr("class","node").attr("transform",function(t){return"translate("+t.x+","+t.y+")"});y.append("circle").attr("id",function(t){return t.id}).attr("r",function(t){return t.r}).style("fill",function(t){return u(t.first_letter)}),y.append("clipPath").attr("id",function(t){return"clip-"+t.id}).append("use").attr("xlink:href",function(t){return"#"+t.id}),y.append("text").on("mouseover",function(t){var e=[];for(var r in t.other_data)e.push({title:t.other_data[r].title,url:t.other_data[r].url});var a=d3.select(".info-box-inner-data-text").selectAll("p").data(e);a.exit().remove(),a.enter().append("p").merge(a).call(function(e){d3.select(".info-box-wrapper").classed("hidden",!1).style("font-weight","bolder").style("font-size","11px").style("top",t.y+10+d3.select("#top-panel")._groups[0][0].offsetHeight+"px").style("left",t.x+100+d3.select("#left-panel")._groups[0][0].offsetWidth+"px")}).text(function(t){return t.title})}).on("click",function(t){var e=!1;if(d3.selectAll("#bookshelf li").each(function(r){for(var a in t.other_data)if(r.title==t.other_data[a].title)return void(e=!0)}),1!=e){for(var r in t.other_data)v.push({title:t.other_data[r].title,url:t.other_data[r].url,source:t.other_data[r].source,publishedAt:t.other_data[r].publishedAt});0==d3.select("#bookshelf li").size()&&d3.select("#expand-draw").attr("src","collapse.png");var a=d3.select("#bookshelf").selectAll("li").data(v.slice(v.length-t.other_data.length));a.enter().merge(a).append("li").append("a").attr("href",function(t){return t.url}).attr("target","_blank").text(function(t){return t.title}).select(function(){return this.parentNode}).append("br").select(function(){return this.parentNode}).append("span").attr("id","source").style("font-style","italic").style("padding-right","10em").text(function(t){return t.source}).select(function(){return this.parentNode}).append("span").attr("id","date").style("font-size","smaller").style("position","absolute").style("right","5em").text(function(t){var e=t.publishedAt;return null!=e&&(e=new Date(e).toString().split(/\d\d\:/)[0].trim()),e})}}).on("mouseout",function(t){d3.select(".info-box-wrapper").classed("hidden",!0),d3.select(".info-box-inner-data-text").selectAll("p").exit().remove()}).attr("clip-path",function(t){return"url(#clip-"+t.id+")"}).selectAll("tspan").data(function(t){return t.class.split(/(?=[A-Z][^A-Z])/g)}).enter().append("tspan").attr("x",0).attr("y",function(t,e,r){return 13+10*(e-r.length/2-.5)}).text(function(t){return t}),y.append("title").text(function(t){return t.class+"\n"+p(t.value)});d3.select("#expand-draw").on("click",function(t){var e="0px",r="0px",a="",n="";if(d3.select(this).attr("src").includes("collapse")){e="0px",r="0px",overflow="hidden";n="0";a="expand.png"}else{e="100%",r="100%",overflow="scroll";n="1";a="collapse.png"}0!=d3.select("#bookshelf li").size()&&(d3.select("#bookshelf-container").transition().duration(1050).style("height",e).style("width",r).style("opacity",n).transition().delay(1060).style("overflow",overflow),d3.select(this).transition().delay(1060).attr("src",a))})}; /* run on load */ $timeout(function() { $scope.grabArticlesForSource(); }, 100); });