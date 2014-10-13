<%@include file="inc/header.jsp"%>

<script type="text/ecmascript" src="js/d3.v2.min.js"></script>
<script type="text/ecmascript" src="js/user-table-min.js"></script>
<script type="text/ecmascript" src="js/project-table-min.js"></script>
<script type="text/ecmascript" src="js/active-projects-min.js"></script>
<script type="text/ecmascript" src="js/active-users-min.js"></script>
<script type="text/ecmascript" src="js/project-tree-min.js"></script>
<script type="text/ecmascript" src="js/index-min.js"></script>

<style>
.buttonLink {
	color: white;
	background: #55AB69;
	padding: 3px 8px;
	border-radius: 4px;
}
.buttonLink:hover {
	color: white;
}

.node circle {
  fill: #fff;
  stroke: steelblue;
  stroke-width: 1.5px;
}

.node {
  font: small sans-serif;
}

.link {
  fill: none;
  stroke: #ccc;
  stroke-width: 1.5px;
}

#icons {
    margin: 10px 0px;
    clear: both;
    height: 220px;
}

#icons div {
    width: 210px;
    display: inline-block;
    text-overflow: wrap;
    height: 180px;
    color: #666;
    margin: 2px;
    padding: 20px;
    border-radius: 20px;
    border: 8px solid #61a3e4;
    background: whitesmoke;
    margin-right: 10px;
    float: left;
}

#project-tree {
	margin-top: 30px;
	border: 1px solid #ccc;
	clear: both;
	text-align: center;
	width: 822px;
}
</style>
        
<%@include file="inc/split.jsp"%>

<p>Indexing <span id="repositoryCount"></span> repositories with <span id="loc"></span> lines of code in <span id="fileCount"></span> files from <span id="contributorCount"></span> contributors across <span id="commitCount"></span> commits.</p>

<div id="tables">
	<ol id="activeProjects"></ol>
	<ol id="activeUsers"></ol>
	
	<div style="clear: both; width: 97%; font-size: small; color: #ccc">
		<span style="float: left"><a href="#" class="buttonLink">view all projects</a></span>
		<span style="float: right"><a href="#" class="buttonLink">view all users</a></span>
	</div>
</div>

<div id="project-tree"></div>

<%@include file="inc/footer.jsp"%>