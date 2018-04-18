function mult () {
	caller(new requestMaker($('#x').val(),$('#y').val(),$('#hashes').val(), 'mul'));
}

function divide () {
	caller(new requestMaker($('#x').val(),$('#y').val(),$('#hashes').val(), 'div'));
}

function plus() {
	caller(new requestMaker($('#x').val(),$('#y').val(),$('#hashes').val(), 'add'));
}

function subtract() {
	caller(new requestMaker($('#x').val(),$('#y').val(),$('#hashes').val(), 'sub'));
}

function power() {
	caller(new requestMaker($('#x').val(),$('#y').val(),$('#hashes').val(), 'pow'));
}

function requestMaker(x, y, hash, operation) {
	this.x = x;
	this.y = y;
	this.hash = hash;
	this.operation = operation;
}

function populator(message) {
	$('#operationWell').append(`<p style="width:95%;word-break:break-all;">${message.x} ${message.op} ${message.y} = ${message.result}</p>`);
	$('#hashWell').append(`<p style="width:95%;word-break:break-all;">${message.hashAlg}: ${message.hash}</p>`);
}

/*====================================AJAX============================*/

function caller(request) {
	var method = ($('#def').prop("checked")) ? 'POST' : 'GET';
	$('#x').val('');
	$('#y').val('');
	$('#operationWell').empty()
	$('#hashWell').empty();
	$.ajax({
		url: '/api/v1/'+request.operation+'?x='+request.x+'&y='+request.y,
		method: method,
		headers: {'hash-alg': request.hash},
		success: (message) => {populator(message);},
		error: (errMessage) => {$('#hashWell').append(`<p>${errMessage.responseJSON.message}</p>`);}
	});
};
