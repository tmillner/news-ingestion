  var app = angular.module("app", []);
  app.controller("controller", function($scope, $http, $timeout) {
   $http.get("/api/search/repo/sources").then(function(response) { $scope.sources = response.data; }); $scope.radio = { "option": "associated-press" }; $scope.dateRangeError = false; $scope.serverError = false; var request = function() { $scope.fromDate = $("#from_datepicker").datepicker("getDate").toISOString().split('.')[0] + "Z"; $scope.toDate = $("#to_datepicker").datepicker("getDate").toISOString().split('.')[0] + "Z"; $scope.dateRangeError = false; $scope.serverError = false; $http.get("/api/search/repo/" + $scope.radio.option + "/" + $scope.fromDate + "/" + $scope.toDate).then(function(response) { $scope.data = response.data; console.log(response.data); return $scope.renderD3View($scope.data); }, function(response) { if (response.data.message.includes("Sorry")){ $scope.dateRangeError = true; } if (response.data.error != null && response.data.error.includes("Internal Server Error")){ $scope.serverError = true; } }); }; $scope.grabArticlesForSource = function() { request(); }; $scope.renderD3View = function(data) { var keywords={};for(var i in data){for(var j in data[i]['keywords']){var keyword=data[i]['keywords'][j];var dataToPush={'source':data[i]['source'],'publishedAt':data[i]['publishedAt'],'title':data[i]['title'],'url':data[i]['url']};if(keywords[keyword]==undefined){keywords[keyword]=[dataToPush];}else{keywords[keyword]['push'](dataToPush);}}}var d3Valid={'children':[]};for(var k in keywords){var toAdd={};toAdd['keyword']=k;toAdd['source']=keywords[k][0x0]['source'];toAdd['data']=[];for(var article in keywords[k]){toAdd['data']['push'](keywords[k][article]);}d3Valid['children']['push'](toAdd);}var width=0x2bc;var height=0x2bc;d3['select']('svg')['remove']();var svg=d3['select']('#container')['append']('svg')['attr']('width',width)['attr']('height',height)['attr']('font-family','sans-serif')['attr']('font-size',0xa)['attr']('text-anchor','middle');var format=d3['format'](',d');var color=d3['scaleOrdinal'](d3['schemeCategory20c']);var pack=d3['pack']()['size']([width,height])['padding'](1.5);var root=d3['hierarchy'](d3Valid)['sum'](function(a){if(a['data']!=undefined){return a['data']['length'];}else{return 0x0;}})['each'](function(a){a['id']=a['data']['keyword']&&a['data']['keyword']['replace'](/ /g,'');a['package']=a['data']['source'];a['class']=a['data']['keyword'];a['first_letter']=a['data']['keyword']&&a['data']['keyword']['charAt'](0x0)['toLowerCase']();a['other_data']=a['data']['data'];});var links=[];var node=svg['selectAll']('.node')['data'](pack(root)['leaves']());node['exit']()['remove']();node['exit']()['selectAll']('circle')['remove']();node['exit']()['selectAll']('title')['remove']();node['exit']()['selectAll']('clipPath')['remove']();node['exit']()['selectAll']('text')['remove']();var group=node['enter']()['append']('g')['merge'](node)['attr']('class','node')['attr']('transform',function(a){return'translate('+a['x']+','+a['y']+')';});group['append']('circle')['attr']('id',function(a){return a['id'];})['attr']('r',function(a){return a['r'];})['style']('fill',function(a){return color(a['first_letter']);});group['append']('clipPath')['attr']('id',function(a){return'clip-'+a['id'];})['append']('use')['attr']('xlink:href',function(a){return'#'+a['id'];});group['append']('text')['on']('mouseover',function(a){var c=[];for(var d in a['other_data']){c['push']({'title':a['other_data'][d]['title'],'url':a['other_data'][d]['url']});}var b=d3['select']('.info-box-inner-data-text')['selectAll']('p')['data'](c);b['exit']()['remove']();b['enter']()['append']('p')['merge'](b)['call'](function(b){d3['select']('.info-box-wrapper')['classed']('hidden',![])['style']('font-weight','bolder')['style']('font-size','11px')['style']('top',a['y']+0xa+d3['select']('#top-panel')['_groups'][0x0][0x0]['offsetHeight']+'px')['style']('left',a['x']+0x64+d3['select']('#left-panel')['_groups'][0x0][0x0]['offsetWidth']+'px');})['text'](function(a){return a['title'];});})['on']('click',function(a){var c=![];d3['selectAll']('#bookshelf\x20li')['each'](function(d){for(var b in a['other_data']){if(d['title']==a['other_data'][b]['title']){c=!![];return;}}});if(c==!![]){return;}for(var b in a['other_data']){links['push']({'title':a['other_data'][b]['title'],'url':a['other_data'][b]['url'],'source':a['other_data'][b]['source'],'publishedAt':a['other_data'][b]['publishedAt']});}if(d3['select']('#bookshelf\x20li')['size']()==0x0){d3['select']('#expand-draw')['attr']('src','collapse.png');}var d=d3['select']('#bookshelf')['selectAll']('li')['data'](links['slice'](links['length']-a['other_data']['length']));d['enter']()['merge'](d)['append']('li')['append']('a')['attr']('href',function(a){return a['url'];})['attr']('target','_blank')['text'](function(a){return a['title'];})['select'](function(){return this['parentNode'];})['append']('br')['select'](function(){return this['parentNode'];})['append']('span')['attr']('id','source')['style']('font-style','italic')['style']('padding-right','10em')['text'](function(a){return a['source'];})['select'](function(){return this['parentNode'];})['append']('span')['attr']('id','date')['style']('font-size','smaller')['style']('position','absolute')['style']('right','5em')['text'](function(b){var a=b['publishedAt'];if(a!=null){a=new Date(a)['toString']()['split'](/\d\d\:/)[0x0]['trim']();}return a;});})['on']('mouseout',function(b){d3['select']('.info-box-wrapper')['classed']('hidden',!![]);var a=d3['select']('.info-box-inner-data-text')['selectAll']('p');a['exit']()['remove']();})['attr']('clip-path',function(a){return'url(#clip-'+a['id']+')';})['selectAll']('tspan')['data'](function(a){return a['class']['split'](/(?=[^a-z-])/g);})['enter']()['append']('tspan')['attr']('x',0x0)['attr']('y',function(c,a,b){return 0xd+(a-b['length']/0x2-0.5)*0xa;})['text'](function(a){return a;});group['append']('title')['text'](function(a){return a['class']+'\x0a'+format(a['value']);});var drawer=d3['select']('#expand-draw')['on']('click',function(f){var e=d3['select'](this)['attr']('src');var b='0px';var c='0px';var d='';var a='';if(e['includes']('collapse')){b='0px';c='0px';overflow='hidden';var a='0';d='expand.png';}else{b='100%';c='100%';overflow='scroll';var a='1';d='collapse.png';}if(d3['select']('#bookshelf\x20li')['size']()==0x0){return;}d3['select']('#bookshelf-container')['transition']()['duration'](0x41a)['style']('height',b)['style']('width',c)['style']('opacity',a)['transition']()['delay'](0x424)['style']('overflow',overflow);d3['select'](this)['transition']()['delay'](0x424)['attr']('src',d);}); }; /* run on load */ $timeout(function() { $scope.grabArticlesForSource(); }, 100);
  });