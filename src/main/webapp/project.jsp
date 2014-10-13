<%@include file="inc/header.jsp"%>

<script type="text/ecmascript" src="js/d3.v2.min.js"></script>
<script type="text/ecmascript" src="js/punchcard-min.js"></script>
<script type="text/ecmascript" src="js/commit-graph-min.js"></script>
<script type="text/ecmascript" src="js/user-table-min.js"></script>
<script type="text/ecmascript" src="js/project-min.js"></script>

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
	
	#summaryTable td {
	    min-width: 140px;
	    padding: 3px 0px;
	}
</style>

<%@include file="inc/split.jsp"%>

<br/>

<h2><%= request.getParameter("name") %></h2>

<p id="fetchurl"></p>

<h3># Project summary</h3>

<div id="languages">&nbsp;</div>

<table id="summaryTable">
	<tr>
		<td>Contributors:</td><td id="contributors"></td>
	</tr>
	<tr>
		<td>Files:</td><td id="files"></td>
	</tr>
	<tr>
		<td>Commits:</td><td id="commits"></td>
	</tr>
	<tr>
		<td>Lines of code:</td><td id="linesOfCode"></td>
	</tr>
	<tr>
		<td>Basic COCOMO:</td><td id="cocomo"></td>
	</tr>
</table>

<h3># Links</h3>

<table>
	<tr>
		<td>Licence:</td><td></td>
	</tr>
	<tr>
		<td>Issue tracker:</td><td></td>
	</tr>
	<tr>
		<td>Code review:</td><td></td>
	</tr>
	<tr>
		<td>Discussions:</td><td></td>
	</tr>
</table>

<h3># Community</h3>

<div id="topContributors">
	<p class="description">Top contributors over the previous year:</p>
	<ol id="topContributorList">
	</ol>
</div>

<h3># Activity</h3>

<p class="description">Commits over the previous year:</p>

<div id="commitGraph"></div>

<p class="description">A year of commit activity by time and day:</p>

<div id="punchcard"></div>

<h3># Readme</h3>

<pre id="readme"></pre>

<script type="text/ecmascript">
	$(document).ready(function() {
		new Project('<%= request.getParameter("name") %>');
	});
</script>
            
<%@include file="inc/footer.jsp"%>