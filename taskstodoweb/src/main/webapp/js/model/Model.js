function Node(id, title, children, taskType) {
	this.data = {'title' : title};
	this.attr = {'id':id, 'rel': taskType};
	this.children = children;
}

function Keyword(keywordId, value){
	this.id = keywordId;
	this.value = value;
}

function Note(noteId, title, content, creationDate, lastModifiedDate){
	this.id = noteId;
	this.title = title;
	this.content = content;
	this.creationDate = handleDateFormat(creationDate);
	this.lastModifiedDate = handleDateFormat(lastModifiedDate);
}

function FileLoad(id, title, filename, description){
	this.id = id;
    this.title = title;
    this.filename = filename;
	this.description = description;
	this.image = {};
}


function Task(id, title, childTasks, taskType, description, priority, startDate, endDate, fileUploads, keywords, notes, finished, searchItems, parentId){
	this.id = id;
	this.title = title;
	this.childTasks = childTasks;
	this.taskType = taskType;
	this.description = description;
	this.priority = priority;
	this.startDate = handleDateFormat(startDate);
	this.endDate = handleDateFormat(endDate);
	this.fileUploads = fileUploads;
	this.keywords = keywords;
	this.notes = notes;
	this.finished = finished;
	this.searchItems = searchItems;
	this.parentId = parentId;
}

function User(id, firstname, surname, username, password, mail, roles){
	this.id = id;
	this.firstname = firstname;
	this.surname = surname;
	this.username = username;
	this.password = password;
	this.mail = mail;
	this.roles = roles;
}

function Role(id){
	this.id = id;
}

function SearchItem(id, value, date){
	this.id = id;
	this.value = value;
	this.date = handleDateFormat(date);
}

function Result(value, url){
	this.url;
	this.value;
}

function ColorItem(colorCode, value, rank, url){
	this.colorCode = colorCode;
	this.value = value;
	this.rank = rank;
	this.url = url;
}
