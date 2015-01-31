//consider as builder
function extractTasks(payload){
	if(payload == null || payload.length == 0)
		return null;
	var tasks = new Array();
	
	for(var i = 0; i < payload.length; i++) {
		var value = payload[i];
		var task = new Task(value.id, value.title, extractTasks(value.childTasks), value.taskType,
							value.description, value.priority, value.startDate, value.endDate, 
							asFileUploadArray(value.fileUploads), asKeywordArray(value.keywords),
							asNoteArray(value.notes), value.finished, asSearchItemArray(value.searchItems), value.parentId);
		tasks.push(task);
	}
	return tasks;
}

function asFileUploadArray(fileUploads){
	if(fileUploads != null){
		var fileUploadsResult = new Array();
		for(var i = 0; i < fileUploads.length; i++) {
			var value = fileUploads[i];
			var fileLoad = new FileLoad(value.id, value.title, value.description);
			fileUploadsResult.push(fileLoad);
		}
		return fileUploadsResult;
	}
}

function asRoleArray(roles){
	if(roles != null){
		var rolesResult = new Array();
		for(var i = 0; i < roles.length; i++) {
			var value = roles[i];
			var role = new Role(value.id);
			rolesResult.push(role);
		}
		return rolesResult;
	}
}

function asKeywordArray(keywords){
	if(keywords != null){
		var keywordsResult = new Array();
		for(var i = 0; i < keywords.length; i++) {
			var value = keywords[i];
			var keyword = new Keyword(value.id, value.value);
			keywordsResult.push(keyword);
		}
		return keywordsResult;
	}
}

function asNoteArray(notes){
	if(notes != null){
		var notesResult = new Array();
		for(var i = 0; i < notes.length; i++) {
			var value = notes[i];
			var note = new Note(value.id, value.title, value.content, value.creationDate, value.lastModifiedDate);
			notesResult.push(note);
		}
		return notesResult;
	}
}

function asSearchItemArray(searchItems){
	if(searchItems != null){
		var searchItemsResult = new Array();
		for(var i = 0; i < searchItems.length; i++) {
			var value = searchItems[i];
			var searchItem = new SearchItem(value.id, value.value, value.date);
			searchItemsResult.push(searchItem);
		}
		return searchItemsResult;
	}
}

function extractNodes(taskTree){
	if(taskTree == null || taskTree.length == 0)
		return null;
	var nodes = new Array(); 
	for(var i = 0; i < taskTree.length; i++) {
		var value = taskTree[i];
		var node = new Node(value.id, value.title, extractNodes(value.childTasks), value.taskType);
		nodes.push(node);
	}
	return nodes;
}