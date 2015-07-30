
//Variable which holds all data
var taskTreeRoot = new Array();
var typeaheadKeywords = null;

function initForm(){
	
	window.resizeTo(300,300);
	
	tinymce.init({
	    selector: "#noteDetail"
	});
	
//	bindSuggestionsForDomain('title', $('#title'));
//	bindSuggestionsForDomain('keyword', $('#keywordValue'));
	
	$('#startDate').datepicker({});
    $('#endDate').datepicker({});
    
    initTree();
    $('#noteTable').dataTable({
    	"bPaginate": false,
    	"bFilter": false,
    	"bInfo" : false,
    	"sScrollY": "200px"
     });
    
    $('#uploadTable').dataTable({
    	"bPaginate": false,
    	"bFilter": false,
    	"bInfo" : false,
    	"sScrollY": "200px"
     });
    
    $('#keywordTable').dataTable({
    	"bPaginate": false,
    	"bFilter": false,
    	"bInfo" : false,
    	"sScrollY": "100px"
     });
   renderNoteTable();
   renderUploadTable();
   renderKeywordTable();
   window.resizeTo(1024, 768);
}


//function bindSuggestionsForDomain(domain, inputField){
//	callRestService('GET', '../spring/keyword/getSuggestionsForDomain/' + domain, null,
//	    function(response) {
//			var anExcitedSource = function(query, cb) {
//				  var results = $.map(response, function(appendage) {
//				    var datum = { theValue: appendage };
//				    return datum;
//				  });
//				  cb(results);
//				};
//	
//			inputField.typeahead(null, {
//				 displayKey: 'theValue',
//				 source: anExcitedSource
//			});
//	    },  
//	    function(xhr, status) {
//	    	showMsg('#errorMsg', 1000, '<strong>ERROR!</strong> Alles schlecht!');
//	    }
//	  );
//	
//}


function initTree(){
	callRestService('GET', '../spring/task/findAll/', null,
	    function(response) {
	    	mapTaskToTreeNode(response);
	    	showMsg('#successMsg', 1000, '<strong>INFO!</strong> Tree init!');
	    },  
	    function(xhr, status) {
	    	showMsg('#errorMsg', 1000, '<strong>ERROR!</strong> An Error occured!');
	    }
	  );
	  return false;
}


function handleEmptyTree(extractedTasks){
	if(extractedTasks == null) {
		return new Array(); 
	}else{
		return extractNodes(taskTreeRoot);
	}
}

function mapTaskToTreeNode(response){
	var extractedTasks = extractTasks(response);
	
	if(extractedTasks != null){
		taskTreeRoot = extractedTasks;
	}
	
	$(function () {
		$("#taskTree").jstree({
			"json_data" : {
				"data" : handleEmptyTree(extractedTasks)
			},
			"themes" : {
				"theme" : "default",
				"icons" : true,
			},
			"rules" : {
				"dragrules" : [ "folder * folder", "tree-drop * folder" ]
			},
			"types" : {
				"types" : {
					"TASKGROUP" : {
						"icon" : { 
							"image" : "../img/taskGroup.png" 
						},
					}/*,
					"SEARCHTASK" : {
						"icon" : { 
							"image" : "../img/searchTask.png" 
						},
					},
					"WORKTASK" : {
						"icon" : { 
							"image" : "../img/workTask.png" 
						},
					}*/
				}
			},
			"plugins" : [ "themes", "json_data", "ui", "dnd", "crrm", "types"]
		}).bind("select_node.jstree", function (e, data) {
			handleSelectNode(e, data);
			}
		).bind("move_node.jstree", function (e, data) {
			handleReassignNode(e, data);
			}
		).bind("create.jstree", function (e, data) {
			handleCreateNode(e, data);
		})
		.bind("remove.jstree", function (e, data) {
			handleRemoveNode(e, data);
		});
	});
}



function refreshTaskView(task){

	$('#historyList').empty();
	if(task.searchItems != null){
		for(var i = 0; i < task.searchItems.length; i++){
			$('#historyList').append($('<li>').
					append($('<span>').attr('class', 'list-group-item').
							append(task.searchItems[i].value)));    
		}
	}
	
	$('#titleReadonly').val(task.title);
	$('#startDateReadonly').val(task.startDate);
	$('#endDateReadonly').val(task.endDate);
	$('#priorityReadonly').val(task.priority);
	$('#descriptionReadonly').val(task.description);
	$('#finished').attr('checked',(task.finished == "true") ? true : false);
	
	renderNoteTable();
	renderUploadTable();
	renderKeywordTable();

	renderSearchHistory();
	
	/*
	//taskspecific fields
	if(task.taskType == 'WORKTASK'){
		$(".workTask").show();
		$(".searchTask").hide();
	} else if (task.taskType == 'SEARCHTASK') {
		$(".workTask").hide();
		$(".searchTask").show();
	} else{
		$(".workTask").hide();
		$(".searchTask").hide();
	}*/
}


function reassignNode(sourceNodeId, movedNodeId, targetNodeId){
	nodeCombination = {
			"sourceNodeId": sourceNodeId,
			"movedNodeId" : movedNodeId,
			"targetNodeId": targetNodeId
		};
	
	callRestService( 'PUT', '../spring/task/reassignNode', nodeCombination,
			function(result) {
		    	if(sourceNodeId != null){
		    		var sourceTask = getTaskByNodeId(sourceNodeId);
		    		var index = getItemArrayLocation(sourceTask.childTasks, sourceNodeId);
		    		if(index != -1){
		    			sourceTask.childTasks.splice(index, 1);
		    		}
		    	}
	    	
		    	if(targetNodeId != null){
		    		var targetTask = getTaskByNodeId(targetNodeId);
		    		if(targetTask.childTasks == null){
		    			targetTask.childTasks = new Array();
		    		}
		    		targetTask.childTasks.push(getTaskByNodeId(movedNodeId));
		    	}
			},  
		    function() {
		    	showMsg('#errorMsg', 1000, '<strong>ERROR!</strong> An error occured!');
		    	$.jstree.rollback(data.rlbk);
		    }
	  );
	  return false;
}

function createNote(){
	var task = $('#taskTree').jstree('get_selected');
	var taskId = task.attr('id');
	var noteTitle = $('#noteTitle').val();
	var noteDetail = tinyMCE.activeEditor.getContent();
	
	note = {
			"id"       		: null,
			"title"			: noteTitle,
			"content" 		: noteDetail,
			"creationDate" 	: null,
			"lastModifiedDate" 	: null,
		};
	
	
	callRestService( 'POST', '../spring/note/create/' + taskId, note,
		function(response) {
			var note = new Note(response.id, noteTitle, noteDetail, response.creationDate, null);
			var owningTask = getTaskByNodeId(taskId);
	    	if(owningTask != null){
	    		if(owningTask.notes == null){
	    			owningTask.notes = new Array();
	    		}
	    		owningTask.notes.push(note);
	    		
	    	}
	    	renderNoteTable();
	    },  
	    function() {
	    	showMsg('#errorMsg', 1000, '<strong>ERROR!</strong> An error occured!');
	    }
	  );
	$ ("#noteModal").removeData ('modal');
	return false;
}

function createTask(){
	$("#taskTree").jstree("create");
}


function deleteTask(){
	$("#taskTree").jstree("remove");
}



function showCreateTaskModal(){
	$('#modalCreateTaskButton').show();
	$('#modalUpdateTaskButton').hide();
	
	$('#startDate').datepicker('setValue', new Date());
	$('#endDate').datepicker('setValue', new Date().getDate() + 1);
	
//	$('input:radio[name=taskType]')[0].checked = true;
	
	$('#modalTask').modal('show');
}



function closeTaskModal(){
	
	$('#title').val("");
	$('#description').val("");
	$('#priority').val("LOW");
	$('#startDate').datepicker(null);
	$('#endDate').datepicker(null);
	
//	$('input:radio[name=taskType]')[0].checked = true;
//	$('input:radio[name=taskType]')[1].checked = false;
//	$('input:radio[name=taskType]')[2].checked = false;
	
//	$("#workTaskSection").hide();
//	$("#searchTaskSection").hide();
	
	$('#modalTask').modal('show');
}


function showUpdateTaskModal(){
	
	$('#modalCreateTaskButton').hide();
	$('#modalUpdateTaskButton').show();
	
	var selectedNode = $('#taskTree').jstree('get_selected');
	var selectedTask = getTaskByNodeId(selectedNode.attr('id'));
	
	
	
	$('#title').val(selectedTask.title);
	$('#description').val(selectedTask.description);
	$('#priority').val(selectedTask.priority);
	$('#finished').attr('checked',(selectedTask.finished == true) ? true : false);
	$('#startDate').datepicker('setValue',selectedTask.startDate);
	$('#endDate').datepicker('setValue',selectedTask.endDate);
	
//	var taskType = selectedNode.attr('rel');
//	if(taskType == "TASKGROUP")
//		$('input:radio[name=taskType]')[0].checked = true;
//	else if(taskType == "WORKTASK")
//		$('input:radio[name=taskType]')[1].checked = true;
//	else if(taskType == "SEARCHTASK")
//		$('input:radio[name=taskType]')[2].checked = true;
//	handleFormType(taskType);
	$('#modalTask').modal('show');
}



function updateTask(){
	
	var selectedNode = $('#taskTree').jstree('get_selected');
	var selectedTask = getTaskByNodeId(selectedNode.attr('id'));
	
	var startDateToTransmit = "";
	if($('#startDate').val() != ""){
		startDateToTransmit = $('#startDate').data('datepicker').getDate().toISOString();
	}
	
	var endDateToTransmit = "";
	if($('#endDate').val() != ""){
		endDateToTransmit = $('#endDate').data('datepicker').getDate().toISOString();
	}
	
	task = {
			"id"			: selectedTask.id,
			"taskType"		: $('input[name="taskType"]:checked').val(),
			"title"       	: $('#title').val(),
			"description" 	: $('#description').val(),
			"priority" 		: $('#priority').val(),
			"startDate" 	: startDateToTransmit,
			"endDate" 		: endDateToTransmit,
			"finished"		: ($('#finished').attr('checked') == 'checked') ? "true" : "false"
		};
	
	callRestService( 'PUT','../spring/task/updateTask', task, 
			function(value) {
//		    	$(selectedNode.attr('rel', $('input[name="taskType"]:checked').val()));
		    	$("#taskTree").jstree('rename_node', selectedNode , $('#title').val() );
		    	var changedTask = getTaskByNodeId(selectedTask.id);
		    	
		    	changedTask.title = 			task.title;
//		    	changedTask.taskType = 			$('input[name="taskType"]:checked').val();
		    	changedTask.description	= 		task.description;
		    	changedTask.priority = 		 	task.priority;
		    	changedTask.startDate =  		handleDateFormat(task.startDate);
		    	changedTask.endDate =  			handleDateFormat(task.endDate);
		    	changedTask.finished = 			task.finished;
		    	refreshTaskView(changedTask);
			},
	    	function() {
		    	showMsg('#errorMsg', 1000, '<strong>ERROR!</strong> An error occured!');
		    	$.jstree.rollback(data.rlbk);
	    	}
	 	);
	  return false;
}




function removeTask(deletedNodeId, parentNodeId){
//	$('#modalpopup').show();
	//TODO: Question
	
	callRestService('DELETE', '../spring/task/deleteByTaskId/' + deletedNodeId, null, 
		function(response) {
			var parentTask = getTaskByNodeId(parentNodeId);
			if(parentTask != null){
				var index = getItemArrayLocation(parentTask.childTasks, deletedNodeId);
	    		if(index != -1){
	    			sourceTask.childTasks.splice(index, 1);
	    		}
			}
		},  
    	function() {
    		showMsg('#errorMsg', 1000, '<strong>ERROR!</strong> An error occured!');
    		$.jstree.rollback(data.rlbk);
    	}
	  );
	  return false;
}


//function handleFormType(taskType){
//	$(function () {
//		if(taskType != "TASKGROUP"){
//			$("#taskSection").show();
//			if(taskType == "SEARCHTASK"){
//				$("#searchTaskSection").show();
//				$("#workTaskSection").hide();
//			}else if (taskType == "WORKTASK") {
//				$("#workTaskSection").show();
//				$("#searchTaskSection").hide();
//			}
//		} else{
//			$("#taskSection").hide();
//			$("#searchTaskSection").hide();
//			$("#workTaskSection").hide();
//		}
//	});
//}


function getTaskByNodeId(taskId){
	var selectedTask = null;
	for ( var i = 0; i < taskTreeRoot.length; i++ ) {
		selectedTask = traveseTree(taskTreeRoot[i], taskId);
		if(selectedTask != null)
			break;
	}
	return selectedTask;
}


function traveseTree(taskSubTree, taskId){
	if(taskSubTree == null)
		return null;
	if(taskSubTree.id == taskId)
		return taskSubTree;
	if(taskSubTree.childTasks == null)
		return null;
	for(var i = 0; i < taskSubTree.childTasks.length; i++) {
		var result = traveseTree(taskSubTree.childTasks[i], taskId);
		if(result != null)
			return result;
	}
}

//Handler

function handleSelectNode(e, data){
	var selectedTask = getTaskByNodeId($(data.rslt.obj[0]).attr('id'));
	if(selectedTask != null){
		refreshTaskView(selectedTask);
	}
}



function handleReassignNode(e, data){
	var movedNodeId = data.rslt.o[0].id;
	
	var parentNodeId = null;
	//TODO: pruefen
	if(data.rslt.op[0].id != 'taskTree'){
		parentNodeId = data.rslt.op[0].id;
	}
	
	var targetNodeId = null;
	
	if(data.rslt.cr != -1){
		targetNodeId = data.rslt.cr[0].id;
	}
	reassignNode(parentNodeId, movedNodeId, targetNodeId);
}

function handleRemoveNode(e, data){
	var parentId = null;
	if(data.rslt.parent != -1){
		parentId = data.rslt.parent.attr("id");
	}
	removeTask($(data.rslt.obj).attr('id'), parentId);
}

function handleCreateNode(e, data) {
	//parent = -1, wenn keiner markiert ist => was wenn parentId == null?
	
	var parentId = null;
	
	if(data.rslt.parent != -1){
		parentId = data.rslt.parent.attr("id");
	}
	
	var startDateToTransmit = "";
	if($('#startDate').val() != ""){
		startDateToTransmit = $('#startDate').data('datepicker').getDate().toISOString();
	}
	
	var endDateToTransmit = "";
	if($('#endDate').val() != ""){
		endDateToTransmit = $('#endDate').data('datepicker').getDate().toISOString();
	}
	
	task = {
			"title"       	: $('#title').val(),
//			"taskType"		: $('input[name="taskType"]:checked').val(),
			"description" 	: $('#description').val(),
			"priority" 		: $('#priority').val(),
			"startDate" 	: startDateToTransmit,
			"endDate" 		: endDateToTransmit,
			"keywords"		: $('#keywords').val(),
			"finished"		: $("#finished").is(':checked'),
			"parentId"		: parentId
		};
	
	callRestService('POST', '../spring/task/createTask', task,
	    function(response) {
	    	var newTask = new Task(response.id, response.title, extractTasks(response.childTasks), response.taskType,
	    			response.description, response.priority, response.startDate, 
	    			response.endDate, asFileUploadArray(response.fileUploads), asKeywordArray(response.keywords), asNoteArray(response.notes));
	    	
	    	$(data.rslt.obj).attr('id', 			newTask.id);
	    	$(data.rslt.obj).attr('rel', 			newTask.taskType);
	    	$("#taskTree").jstree('rename_node', 	data.rslt.obj , newTask.title );
	    	if(parentId != null){
		    	var parentTask = getTaskByNodeId(parentId);
		    	if(parentTask.childTasks == null){
		    		parentTask.childTasks = new Array();
		    	}
		    	parentTask.childTasks.push(newTask);
	    	} else{
	    		//if root is firstlevelnode, just add it to the array
	    		taskTreeRoot.push(newTask);
	    	}
	    },  
	    function() {
	    	showMsg('#errorMsg', 1000, '<strong>ERROR!</strong> An error occured!');
	    	$.jstree.rollback(data.rlbk);
	    }
	  );
}

function renderNoteTable(){
	$('#noteTable').dataTable().fnClearTable();
	if(isSelected() != true){
		$('#addNoteButton').attr('disabled', 'disabled');
		$('#addNoteButton').removeAttr("data-toggle");
	} else{
		$('#addNoteButton').button ('enable');
		$('#addNoteButton').attr("data-toggle", "modal");
	}
	var selectedNode = $('#taskTree').jstree('get_selected');
	var selectedTaskId = null;
	if(selectedNode != null){
		selectedTaskId = $(selectedNode).attr('id');
	}
	
	if(selectedTaskId != null && selectedTaskId != 'taskTree'){
		var task = getTaskByNodeId(selectedTaskId);
		var notes = task.notes;
		for ( var index = 0; index < notes.length; index++) {
			$('#noteTable').dataTable().fnAddData( [notes[index].title , 
			                                        '<button class="btn btn-xs" onclick="deleteNote(' + notes[index].id + ',' + index + ');"><i class="glyphicon glyphicon-trash"></i></button>', 
			                                        '<button class="btn btn-xs" onclick="showUpdateNoteModal(' + notes[index].id + ',' + index + ');"><i class="glyphicon glyphicon-pencil"></i></button>'
//			                                        '<a href="#noteUpdateModal" role="button" class="btn btn-mini" data-toggle="modal"><i class="icon-pencil"></i></a>'
			                                        ] 
												 );
		}
		
	}
}


function deleteNote(deletedNoteId, tablePos){
	
	callRestService('DELETE', '../spring/note/delete/' + deletedNoteId, null, 
		function(response) {
			var selectedNode = $('#taskTree').jstree('get_selected');
			var selectedTaskId = null;
			if(selectedNode != null){
				selectedTaskId = $(selectedNode).attr('id');
			}
			var owningTask = getTaskByNodeId(selectedTaskId);
			var index = getItemArrayLocation(owningTask.notes, deletedNoteId);
    		if(index != -1){
    			owningTask.notes.splice(index, 1);
    		}
    		$('#noteTable').dataTable().fnDeleteRow(tablePos);
    		renderNoteTable();
    	},  
    	function() {
    		showMsg('#errorMsg', 1000, '<strong>ERROR!</strong> An error occured!');
    	}
	  );
	  return false;
}

function showCreateNoteModal(){
	$('#createNoteButton').show();
	$('#updateNoteButton').hide();
	$('#noteModal').modal('show');
}


function showUpdateNoteModal(noteId, tablePos){
	
	
	$('#createNoteButton').hide();
	$('#updateNoteButton').show();
	
	var selectedNode = $('#taskTree').jstree('get_selected');
	var selectedTaskId = null;
	if(selectedNode != null){
		selectedTaskId = $(selectedNode).attr('id');
	}
	var owningTask = getTaskByNodeId(selectedTaskId);
	var index = getItemArrayLocation(owningTask.notes, noteId);
	if(index != -1){
	
		$('#noteTitle').val(owningTask.notes[index].title);
		tinyMCE.get('noteDetail').setContent(owningTask.notes[index].content);
		$('#noteUpdateId').val(noteId);
		$('#noteUpdateTablePos').val(tablePos);
		$('#noteModal').modal('show');
	}
}

function updateNote(){
	
	note = {
			"id"       			: $('#noteUpdateId').val(),
			"title"				: $('#noteTitle').val(),
			"content" 			: tinyMCE.get('noteDetail').getContent(),
			"creationDate" 		: null,
			"lastModifiedDate" 	: null,
		};
	
	callRestService('PUT', '../spring/note/update/' , note, 
			updateNoteSucess,  
    	function(error) {
			showMsg('#errorMsg', 1000, '<strong>ERROR!</strong> An error occured!');
    	}
	  );
	 return false;
}


var updateNoteSucess = function(data, textStatus, jqXHR) {
	var selectedNode = $('#taskTree').jstree('get_selected');
	var selectedTaskId = null;
	if(selectedNode != null){
		selectedTaskId = $(selectedNode).attr('id');
	}
	var owningTask = getTaskByNodeId(selectedTaskId);
	var index = getItemArrayLocation(owningTask.notes, note.id);
	if(index != -1){
		owningTask.notes[index] = note;
	}
	renderNoteTable();
}



//pass in an HTML5 ArrayBuffer, returns a base64 encoded string
function arrayBufferToBase64(arrayBuffer) {
   var bytes = new Uint8Array(arrayBuffer);
   var len = bytes.byteLength;
   var binary = '';
   for (var i = 0; i < len; i++) {
      binary += String.fromCharCode( bytes[ i ] );
   }
   return window.btoa( binary );
}

function uploadFile(){
	var file = $('#fileUploader').get(0).files[0];
	var filename = file.name;
	var task = $('#taskTree').jstree('get_selected');
	var taskId = task.attr('id');
	var noteTitle = $('#uploadTitle').val();
	
	var fileLoad = new FileLoad(); 
	fileLoad.title = noteTitle;
	fileLoad.filename = filename;  
	
	var reader = new FileReader();
	reader.onload = function loaded(evt) {
		fileLoad.image={};
		fileLoad.image.bytes = arrayBufferToBase64(evt.target.result);
		fileLoad.image.contentType = file.type;
 
	  $.ajax({
	        url: '../spring/load/upload/' + taskId,
	        data: JSON.stringify(fileLoad),
	        type: 'POST',
	        dataType: 'json',
	        processData: true,
	        contentType: "application/json; charset=utf-8",
	        success:
	        function(response) {
	        	//fileUploadId, title, description, uri, creationDate
	        	fileLoad.id = response.id;
	        	var owningTask = getTaskByNodeId(taskId);
		    	if(owningTask != null){
		    		if(owningTask.fileUploads == null){
		    			owningTask.fileUploads = new Array();
		    		}
		    		owningTask.fileUploads.push(fileLoad);
		    		
		    	}
		    	renderUploadTable();
		    	showMsg('#successMsg', 1000, '<strong>INFO!</strong> Upload successful!');
		    },
	    	error: function(errorMsg) {
	    		showMsg('#errorMsg', 1000, '<strong>ERROR!</strong> An error occured!');
			}
	    });
    };

    reader.readAsArrayBuffer(file);
}


function deleteFile(deletedFileId, tablePos){
	
	callRestService('DELETE', '../spring/load/delete/' + deletedFileId, null, 
		function(response) {
			var selectedNode = $('#taskTree').jstree('get_selected');
			var selectedTaskId = null;
			if(selectedNode != null){
				selectedTaskId = $(selectedNode).attr('id');
			}
			var owningTask = getTaskByNodeId(selectedTaskId);
			var index = getItemArrayLocation(owningTask.fileUploads, deletedFileId);
    		if(index != -1){
    			owningTask.fileUploads.splice(index, 1);
    		}
    		renderUploadTable();
    		showMsg('#successMsg', 1000, '<strong>INFO!</strong> Deleted successfully!');
    	},  
    	function() {
    		showMsg('#errorMsg', 1000, '<strong>ERROR!</strong> An error occured!');
    	}
	  );
	  return false;
}


function renderUploadTable(){
	$('#uploadTable').dataTable().fnClearTable();
	if(isSelected() != true){
		$('#addUploadButton').attr('disabled', 'disabled');
		$('#addUploadButton').removeAttr("data-toggle");
	} else{
		$('#addUploadButton').button ('enable');
		$('#addUploadButton').attr("data-toggle", "modal");
	}
	
	
	var selectedNode = $('#taskTree').jstree('get_selected');
	var selectedTaskId = null;
	if(selectedNode != null){
		selectedTaskId = $(selectedNode).attr('id');
	}
	
	if(selectedTaskId != null && selectedTaskId != 'taskTree'){
		var task = getTaskByNodeId(selectedTaskId);
		var fileUploads = task.fileUploads;
		for ( var index = 0; index < fileUploads.length; index++) {
			$('#uploadTable').dataTable().fnAddData( [fileUploads[index].title , 
			                                        '<button class="btn btn-xs" onclick="deleteFile(' + fileUploads[index].id + ',' + index + ');"><i class="glyphicon glyphicon-trash"></i></button>',
			                                        '<a href="javascript:downloadTaskFile(&quot;../spring/load/download/' + fileUploads[index].id + '&quot;)"><i class="glyphicon glyphicon-download-alt"></i></a>'
			                                        ] 
												 );
		}
	}
}

function downloadTaskFile(url, content){
	 window.open(url, 'Attachment', 'width=500,height=400,left=100,top=200');
}

function renderKeywordTable(){
	$('#keywordTable').dataTable().fnClearTable();
	if(isSelected() != true){
		$('#addKeywordButton').attr('disabled', 'disabled');
		$('#addKeywordButton').removeAttr("data-toggle");
	} else{
		$('#addKeywordButton').button ('enable');
		$('#addKeywordButton').attr("data-toggle", "modal");
	}
	
	
	var selectedNode = $('#taskTree').jstree('get_selected');
	var selectedTaskId = null;
	if(selectedNode != null){
		selectedTaskId = $(selectedNode).attr('id');
	}
	
	if(selectedTaskId != null && selectedTaskId != 'taskTree'){
		var task = getTaskByNodeId(selectedTaskId);
		var keywords = task.keywords;
		for ( var index = 0; index < keywords.length; index++) {
			$('#keywordTable').dataTable().fnAddData( [keywords[index].value , 
			                                        '<button class="btn btn-xs" onclick="deleteKeyword(' + keywords[index].id + ',' + index + ');"><i class="glyphicon glyphicon-trash"></i></button>' 
			                                        ] 
												 );
		}
	}
}


function deleteKeyword(deletedKeywordId, tablePos){
	
	callRestService('DELETE', '../spring/keyword/delete/' + deletedKeywordId, null, 
		function(response) {
			var selectedNode = $('#taskTree').jstree('get_selected');
			var selectedTaskId = null;
			if(selectedNode != null){
				selectedTaskId = $(selectedNode).attr('id');
			}
			var owningTask = getTaskByNodeId(selectedTaskId);
			var index = getItemArrayLocation(owningTask.keywords, deletedKeywordId);
    		if(index != -1){
    			owningTask.keywords.splice(index, 1);
    		}
    		renderKeywordTable();
    		showMsg('#successMsg', 1000, '<strong>INFO!</strong> Deleted successfully!');
    	},  
    	function() {
    		showMsg('#errorMsg', 1000, '<strong>ERROR!</strong> An error occured!');
    	}
	  );
	  return false;
}

function createKeyword(){
	var task = $('#taskTree').jstree('get_selected');
	var taskId = task.attr('id');
	var keywordValue = $('#keywordValue').val();
	
	keyword = {"value"       	: keywordValue};
	
	
	callRestService( 'POST', '../spring/keyword/create/' + taskId, keyword,
		function(response) {
			var keyword = new Keyword(response.id, response.value);
			showMsg('#successMsg', 1000, '<strong>INFO!</strong> Keyword created sucessfully!');
	    	var owningTask = getTaskByNodeId(taskId);
	    	if(owningTask != null){
	    		if(owningTask.keywords == null){
	    			owningTask.keywords = new Array();
	    		}
	    		owningTask.keywords.push(keyword);
	    	}
	    	renderKeywordTable();
	    },  
	    function() {
	    	showMsg('#errorMsg', 1000, '<strong>ERROR!</strong> An error occured!');
	    }
	  );
	$ ("#keywordModal").removeData ('modal');
	return false;
}


function renderSearchHistory(){
	
	
}

function isSelected(){
	var selectedNode = $('#taskTree').jstree('get_selected');
	if(selectedNode != null){
		var selectedTaskId = $(selectedNode).attr('id');
		if(selectedTaskId != null){
			return true;
		}else{
			return false;
		}
	} else{
		return false;
	}
}

function showSearchModal(){
    $('#taskcontextInput').val('')
    $('#primarySearchTable').empty();
    $('#contextSearchTable').empty();
}


function searchTaskContext(){

    var selectedNode = $('#taskTree').jstree('get_selected');
	var selectedTask = getTaskByNodeId(selectedNode.attr('id'));
	
	
	var term = $('#taskcontextInput').val();
	callRestService( 'GET', '../spring/searchitem/search/' + selectedTask.id + '/' + term ,null ,
		function(response) {primarySearchTable
			var table = $('#primarySearchTable');    
			table.empty();
			for ( var index = 0; index < response.primaryResult.length; index++) {
				table.append("<tr><td>" + (index +1) + "</td><td>" + response.primaryResult[index].value + "</td><td><a href='" + response.primaryResult[index].url + "'>" +response.primaryResult[index].url+ "</a></td></tr>");
			}
			table = $('#contextSearchTable'); 
			table.empty();
			for ( var index = 0; index < response.rankedResult.length; index++) {
				table.append("<tr><td>" + (index +1) + "</td><td>" + response.rankedResult[index].value + "</td><td>" + 1 + "</td><td>" + 1 + "</td><td><a href='" + response.rankedResult[index].url + "'>" +response.rankedResult[index].url+ "</a></td></tr>");
			}
		},  
	    function() {
	    	showMsg('#errorMsg', 1000, '<strong>ERROR!</strong> An error occured!');
	    }
	);
}

//function mapRowColorToEntry(primarySearchResult){
//	var colorVector = [	"#FFF2F2","#FDFDF0","#F9FDFF","#FFF9EA","#FAE7EC","#F4F4BF","#FFF284","#FFECB0",
//	                   	"#FFE099","#FFE6B5","#FFD9B7","#FFD7B3","#FFB5B5","#F0B9C8","#FF7DFF","#D881ED","#B7B7FF","#A6DEEE","#CFE7E2"]
//	var colorMapping = new Array();
//	for ( var index = 0; index < primarySearchResult.length; index++) {
//		var randomColor = colorVector[index];
//		colorMapping.push(new ColorItem(randomColor, primarySearchResult[index].value, index + 1, primarySearchResult[index].url));
//	}
//	return colorMapping;
//}
//
//
//function computeRowColorEntryAndRankingDelta(primarySearchResult, rankedResult){
//	var colorMapping = new Array();
//	for ( var i = 0; i < rankedResult.length; i++) {
//		for ( var j = 0; j < primarySearchResult.length; j++) {
//			if(rankedResult[i].value == primarySearchResult[j].value){
//				colorMapping.push(new ColorItem(primarySearchResult[j].colorCode, rankedResult[i].value, primarySearchResult[j].rank - (i+1), rankedResult[i].url));
//				break;
//			}
//		}
//	}
//	return colorMapping;
//}


