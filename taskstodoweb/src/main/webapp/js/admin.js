
var users;
var currentUser;
var dictionaryRoles = {};

function extractUsers(payload){
	if(payload == null || payload.length == 0)
		return null;
	var users = new Array();

	for(var i = 0; i < payload.length; i++) {
		var value = payload[i];
		var user = new User(value.id, value.firstname,
							value.surname, value.username, value.password, 
							value.mail, asRoleArray(value.roles));
		users.push(user);
	}
	return users;
}

function initForm(){
	
	callRestService('GET', '../spring/admin/findAll/', null,
	    function(response) {
			users = extractUsers(response);
			renderUserTable();
			showMsg('#successMsg', 1000, '<strong>INFO!</strong> Tree init!');
	    },  
	    function() {
	    	showMsg('#errorMsg', 1000, '<strong>ERROR!</strong> Alles schlecht!');
	    }
	  );
	
    $('#userTable').dataTable({
    	"bPaginate": false,
    	"bFilter": false,
    	"bInfo" : false,
    	"sScrollY": "200px",
     });

   
    
    $('#roleTable').dataTable({
    	"bPaginate": false,
    	"bFilter": false,
    	"bInfo" : false,
    	"sScrollY": "200px"
     });
    
    callRestService('GET', '../spring/admin/findAllRoles/', null,
	    function(response) {
    		for(var i = 0; i < response.length; i++) {
				$("<option/>").val(response[i].id).text(response[i].title).appendTo('#availableRolesSelect');
				dictionaryRoles[response[i].id] = response[i].title;
    		}
	    },  
	    function() {
	    	showMsg('#errorMsg', 1000, '<strong>ERROR!</strong> Alles schlecht!');
	    }
    );
  
}



function createUser(){

	user = {
			"id"       		: null,
			"firstname"		: $('#firstname').val(),
			"surname" 		: $('#surname').val(),
			"username" 		: $('#username').val(),
			"password" 		: $('#password').val(),
			"mail"			: $('#mail').val(),
			"roles"			: null	
	};
	
	callRestService( 'POST', '../spring/admin/create/', user,
		function(response) {
			var user = new User(response.id, response.firstname, response.surname, response.username, response.password, response.mail);
			showMsg('#successMsg', 1000, '<strong>INFO!</strong> Alles super!');
			users.push(user);
	    	renderUserTable();
	    },  
	    function() {
	    	showMsg('#errorMsg', 1000, '<strong>ERROR!</strong> Alles schlecht!');
	    }
	  );
	return false;
}

function renderUserTable(){
	$('#userTable').dataTable().fnClearTable();
	for ( var index = 0; index < users.length; index++) {
			$('#userTable').dataTable().fnAddData( [users[index].id, users[index].username , 
			                                        '<button class="btn btn-xs" onclick="deleteUser(' + users[index].id + ',' + index + ');"><i class="glyphicon glyphicon-trash"></i></button>', 
			                                        '<button class="btn btn-xs" onclick="showUpdateUserModal(' + users[index].id + ');"><i class="glyphicon glyphicon-pencil"></i></button>'
			                                        ] 
												 );
		}
	
	$('#userTable').dataTable().$('tr').click( function () {
		$('#userTable').find('.selected').each(function(){
			  $(this).removeClass("selected");
		});
		$(this).addClass('selected');
		var data = $('#userTable').dataTable().fnGetData(this);
		var index = getItemArrayLocation(users, data[0]);
		if(index != -1){
			selectUser(users[index]);
		}
	    
	} );
}


function renderRoleTable(){
	$('#roleTable').dataTable().fnClearTable();
	//obtain selected user
	if(currentUser.roles != null){
		for ( var index = 0; index < currentUser.roles.length; index++) {
			//TODO: Freeze window, aufräumen
			$('#roleTable').dataTable().fnAddData( [dictionaryRoles[currentUser.roles[index].id], 
			                                        '<button class="btn btn-xs" onclick="deleteRoleFromUser(' + currentUser.id + ',' + currentUser.roles[index].id + ');"><i class="glyphicon glyphicon-trash"></i></button>', 
			                                        ] 
												 );
			
		}
		$('#roleTable').dataTable().$('tr').click( function () {
			$('#roleTable').find('.selected').each(function(){
				  $(this).removeClass("selected");
			});
			$(this).addClass('selected');
		} );
	}
}
	

function showUpdateUserModal(userId){
	$('#roleModal').modal('show');
}

function addRoleToUser(userId, roleId){
	var roleId = $('#availableRolesSelect').val();
	callRestService('POST', '../spring/admin/addRoleToUser/' + currentUser.id + '/' + roleId, null, 
		function(response) {
			var index = getItemArrayLocation(users, currentUser.id);
    		if(index != -1){
    			if(currentUser.roles == null){
    				currentUser.roles = new Array();
    			}
    			currentUser.roles.push(new Role(roleId)); 
    			renderRoleTable();
    			showMsg('#successMsg', 1000, '<strong>INFO!</strong> Tree init!');
    		}
    	},  
    	function() {
    		showMsg('#errorMsg', 1000, '<strong>ERROR!</strong> Alles schlecht!');
    	}
	  );
	  return false;
}

function deleteRoleFromUser(userId, roleId){
	callRestService('DELETE', '../spring/admin/deleteRoleFromUser/' + userId + '/' + roleId, null, 
		function(response) {
			currentUser.roles.splice(getItemArrayLocation(currentUser, roleId), 1);
			renderRoleTable();
		},  
    	function() {
    		showMsg('#errorMsg', 1000, '<strong>ERROR!</strong> Alles schlecht!');
    	}
	  );
	  return false;
}

function deleteUser(deletedUserId, tablePos){
	callRestService('DELETE', '../spring/admin/delete/' + deletedUserId, null, 
		function(response) {
			var index = getItemArrayLocation(users, deletedUserId);
    		if(index != -1){
    			users.splice(index, 1);
    		}
    		$('#userTable').dataTable().fnDeleteRow(tablePos);
    		renderUserTable();
    		showMsg('#successMsg', 1000, '<strong>INFO!</strong> Tree init!');
    	},  
    	function() {
    		showMsg('#errorMsg', 1000, '<strong>ERROR!</strong> Alles schlecht!');
    	}
	  );
	  return false;
}

function selectUser(user){
	$('#indicator').show();
	//TODO: highlight selected
	currentUser = user;
	renderRoleTable();
}
