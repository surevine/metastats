<%@include file="inc/header.jsp"%>

<%@include file="inc/split.jsp"%>

<br/>

<p class="description">The following table enumerates the available API calls and links to examples:</p>

<div id="tables">
	<ol id="apis">
		<li>
			<a href="api/init/all">Initialisation - All</a>
		</li>
		<li>
			<a href="api/projects/active">Projects - Active</a>
		</li>
		<li>
			<a href="api/projects/all">Projects - All</a>
		</li>
		<li>
			<a href="api/projects/named/git">Projects - Named</a>
		</li>
		<li>
			<a href="api/users/active">Users - Active</a>
		</li>
		<li>
			<a href="api/users/all">Users - All</a>
		</li>
		<li>
			<a href="api/users/id/spearce@spearce.org">Users - Id</a>
		</li>
	</ol>
</div>

<p>They're all GET requests returning JSON.</p>
            
<%@include file="inc/footer.jsp"%>