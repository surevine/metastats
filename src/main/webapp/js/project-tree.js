function ProjectTree() {
	var diameter = 720;
	
	var tree = d3.layout.tree()
	    .size([360, diameter / 2 - 120])
	    .separation(function(a, b) { return (a.parent == b.parent ? 1 : 2) / a.depth; });
	
	var diagonal = d3.svg.diagonal.radial()
	    .projection(function(d) { return [d.y, d.x / 180 * Math.PI]; });
	
	var svg = d3.select("#project-tree").append("svg")
	    .attr("width", diameter)
	    .attr("height", diameter)
	    .append("g")
	    .attr("transform", "translate(" + diameter / 2 + "," + diameter / 2 + ")");
	
	$.getJSON('api/projects/all', function(data) {
	  var root = {};
	  root.name = "";
	  root.children = [];
	  
	  for (var i = 0; i<data.length; i++) {
		  var lang = null;
		  for (var j in root.children) {
			  if (root.children[j].name && root.children[j].name === data[i].primaryLanguage) {
				  lang = root.children[j];
				  break;
			  }
		  }
		  
		  if (!lang) {
			  lang = {};
			  lang.name = data[i].primaryLanguage;
			  lang.children = [];
			  root.children.push(lang);
		  }
		  
		  var child = {};
		  child.name = data[i].name;
		  lang.children.push(child);
	  }
		
	  var nodes = tree.nodes(root),
	      links = tree.links(nodes);
	
	  var link = svg.selectAll(".link")
	      .data(links)
	      .enter().append("path")
	      .attr("class", "link")
	      .attr("d", diagonal);
	
	  var node = svg.selectAll(".node")
	      .data(nodes)
	      .enter().append("g")
	      .attr("class", "node")
	      .attr("transform", function(d) { return "rotate(" + (d.x - 90) + ")translate(" + d.y + ")"; })
	
	  node.append("circle")
	      .attr("r", 4.5);
	
	  node.append("text")
	      .attr("dy", ".31em")
	      .attr("text-anchor", function(d) { return d.x < 180 ? "start" : "end"; })
	      .attr("transform", function(d) { return d.x < 180 ? "translate(8)" : "rotate(180)translate(-8)"; })
	      .text(function(d) { return d.name; });
	});
	
	d3.select(self.frameElement).style("height", diameter + "px");
}