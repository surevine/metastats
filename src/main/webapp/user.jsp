<%@include file="inc/header.jsp"%>

<script type="text/ecmascript" src="js/d3.v2.min.js"></script>
<script type="text/ecmascript" src="js/punchcard-min.js"></script>
<script type="text/ecmascript" src="js/commit-graph-min.js"></script>
<script type="text/ecmascript" src="js/project-table-min.js"></script>
<script type="text/ecmascript" src="js/user-min.js"></script>

<style>
	.arc path {
	  stroke: #fff;
	}
	.axis path,
	.axis line {
	    display: none;
	}
	.axis text {
	    font-family: sans-serif;
	    font-size: 11px;
	}
	.loading {
	    font-family: sans-serif;
	    font-size: 15px;
	}
	.circle {
	    fill: #999;
	}
	h2#name {
		display: inline-block;
		margin-bottom: 0px;
	}
	
	#commitGraph {
		border: 1px solid #CCC;
		background: whiteSmoke;
		padding: 10px;
		width: 720px;
	}
	
	#commitGraph .axis path,
	#commitGraph .axis line {
	  fill: none;
	  stroke: #000;
	  shape-rendering: crispEdges;
	}
	
	#commitGraph .bar {
	  fill: steelblue;
	}
	
	#commitGraph .x.axis path {
	  display: none;
	}
	
	#punchcard {
		width: 720px;
		padding: 10px;
		border: 1px solid #ccc;
		background: whitesmoke;
	}
</style>

<%@include file="inc/split.jsp"%>

<br/>

<div>
	<div style="float: left; border: 3px solid #ccc; border-radius: 48px; padding: 10px; width: 48px; height: 48px">
		<img src="/monster/?seed=<%= request.getParameter("id") %>&size=48" width="48px" />
	</div>
	<div style="float: left; margin-top: 20px; margin-left: 10px">
		<h2 id="name"></h2><span id="score" class="score"></span>
	</div>
</div>

<br style="clear: both"/>

<h3># Projects summary</h3>

<p class="description">List of projects:</p>

<div id="projects">
	<ol id="projectList">
	</ol>
</div>

<h3># Activity</h3>

<p class="description">Commits over the previous year:</p>

<div id="commitGraph"></div>

<p class="description">A year of commit activity by time and day:</p>

<div id="punchcard"></div>

<script type="text/ecmascript">
	$(document).ready(function() {
		new User('<%= request.getParameter("id") %>');
	});
</script>

<%@include file="inc/footer.jsp"%>