<%@include file="inc/header.jsp"%>

<style>
input[type=text] {
	width: 360px;
	padding: 5px;
}
</style>

<%@include file="inc/split.jsp"%>

<br/>

<p>Enter the URL for the project to add:</p>

<form id="addForm" method="POST" action="api/init/add">
	<input type="text" id="url" name="url" />
	<input id="addFormSubmit" type="submit" value="Add" />
</form>

<script type="text/ecmascript">
	$('#addFormSubmit').click(function() {
		$.post('api/init/add', {
			data: $('#addForm').serialize()
		});
	});
</script>

<%@include file="inc/footer.jsp"%>