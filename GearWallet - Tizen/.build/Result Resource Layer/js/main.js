/*    
 * Copyright (c) 2014 Samsung Electronics Co., Ltd.   
 * All rights reserved.   
 *   
 * Redistribution and use in source and binary forms, with or without   
 * modification, are permitted provided that the following conditions are   
 * met:   
 *   
 *     * Redistributions of source code must retain the above copyright   
 *        notice, this list of conditions and the following disclaimer.  
 *     * Redistributions in binary form must reproduce the above  
 *       copyright notice, this list of conditions and the following disclaimer  
 *       in the documentation and/or other materials provided with the  
 *       distribution.  
 *     * Neither the name of Samsung Electronics Co., Ltd. nor the names of its  
 *       contributors may be used to endorse or promote products derived from  
 *       this software without specific prior written permission.  
 *  
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS  
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT  
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR  
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT  
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,  
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT  
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,  
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY  
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE  
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

var GALL_THM_RQST = 'gallery-thumbnail-req';
var GALL_THM_RESP = 'gallery-thumbnail-rsp';
var GALL_IMG_RQST = 'gallery-image-req';
var GALL_IMG_RESP = 'gallery-image-rsp';
var GALL_PUSH_IND = 'gallery-push-ind';
var FREQ_ICR	  = 'frequency-increment';

var POINTCARD = 0;
var CREDITCARD = 1;

var gLatestOffset = [-1, -1];
var gListviewName = ['#list-pointcard', '#list-creditcard'];
var gListType = ['pointcard', 'creditcard'];

var gListIndex = 0;

/* Alert message with toast popup of TAU */
function toastAlert(msg) {
	var toastMsg = document.getElementById("popupToastMsg");
	toastMsg.innerHTML = msg;
	// set toast message content with msg and openPopup
	tau.openPopup('#popupToast');
	console.log(msg);
}

/* callback: SAP init success */
var sapinitsuccesscb = {		
	onsuccess : function() {
		console.log('Succeed to connect');
		requestList();
	},
	ondevicestatus : function(status) {
		if (status == "DETACHED") {
			toastAlert('Detached remote peer device');
			clearList();
		} else if (status == "ATTACHED") {
			reconnect();
		}
	},
	onlistener : function(respData) {
		switch (respData.msgId) {
			case GALL_PUSH_IND:
				gLatestOffset = -1;
				$('.ui-listview').empty();
				requestList();
				return true;
			default:
				return false;
		}
	}
};

/* Initialize main page */
function initialize() {
	sapInit(sapinitsuccesscb, function(err) {
		console.log(err.name);
		if (err.name == "PEER_DISCONNECTED") {
			toastAlert(err.message);
			clearList(true);
		} else {
			toastAlert('Failed to connect to service');
			clearList();
		}
	});
}

/* Request and get list of image from host device */
function requestList() {
	var reqData = {
	    'msgId' : GALL_THM_RQST,
	    'offset' : gLatestOffset[gListIndex],
	    'listType' : gListType[gListIndex]
	};
	
	console.log('request List : ' + gListIndex +' , ' + reqData.offset + ', ' + reqData.listType);
	
	sapRequest(reqData, function(respData) {
		var count = respData.count;
		var list = respData.list;

		$('#list-' + gListType[gListIndex] + ' li:last-child').remove();

		for (var i = 0; i < count; i++) {
			var id = list[i].id,
				image = list[i].image,
				fileName = list[i].name,
				color = list[i].color;
			
			console.log('sapRequest success : ' + color);

			$('#list-' + gListType[gListIndex]).append(
			        '<li style="background-image:url(res/background_slot.png); background-color:' + color + '">' + 
			        '<a onclick="requestImage(' + id + ' , \'' + gListType[gListIndex] + '\' , \'' + fileName + '\');"' + 
			        'style="color: #000000">' + fileName.split('.png')[0] + '</a></li>');
		}
		
		$('#list-' + gListType[gListIndex]).append('<li style="background-image:url(res/background_end.png)">' 
												   + '<a onclick="refresh();"></a></li>');

		gListIndex++;
		
		if (gListIndex < 2) {
			requestList();
		}
		
		gLatestOffset[gListIndex] = list[respData.count - 1].id;
	}, function(err) {
		console.log('Failed to get list.');
    });
}

function refresh() {
	console.log('refresh');
	
	clearList(false);
	requestList();
}

function reconnect() {
	$('#list-pointcard').empty();
	$('#list-creditcard').empty();
	sapFindPeer(function(){
		console.log('Succeed to reconnect');
	}, function(err){
		toastAlert('Failed to reconnect to service');
		clearList(true);
    });
}

function clearList(reconnect) {
	console.log('clear List');
	
	gListIndex = 0;
	gLatestOffset[POINTCARD] = -1;
	gLatestOffset[CREDITCARD] = -1;
	
	$('#list-pointcard').empty();
	$('#list-creditcard').empty();
	
	if (reconnect) {
		$('#list-pointcard').append('<input type="button" class="ui-btn ui-inline" value="Connect" onclick="reconnect();" style="width:100%;"/>');
		$('#list-creditcard').append('<input type="button" class="ui-btn ui-inline" value="Connect" onclick="reconnect();" style="width:100%;"/>');
	} else {
		$('#list-pointcard').append('<li>BT Disconnected. Connection waiting...</li>');
	}
}
    
function requestImage(id, listType, fileName) {
	var reqData = {
	    'msgId' : GALL_IMG_RQST,
	    'id' : id,
	    'width' : 320,
	    'height' : 320,
	    'listType' : listType,
	    'fileName' : fileName
	};

	sapRequest(reqData, function(respData) {
		var imageView = document.getElementById("imageView");
		/* set image tag for imageView and changePage to image page */
		imageView.innerHTML = '<img id="viewImage" src="data:image/jpeg;base64,' + respData.image.image + 
							  '" onclick="increaseFreq(' + id + ' , \'' + listType + '\' , \'' + fileName + '\');"/>';
		tau.changePage('#imagePage');
	}, function(err) {
		toastAlert('Failed to get image');
	});
}

function increaseFreq(id, listType, fileName) {
	// 여기에서 sap 쪽을 통해 android로 신호를 줘야한다.
	console.log('increase Freq');
	
	var reqData = {
			'msgId' : FREQ_ICR,
			'id' : id,
			'listType' : listType,
			'fileName' : fileName,
			'frequency' : 1
	};
	
	sapRequest(reqData, function(respData) {
		toastAlert('Success to Send Frequency');
	}, function(err) {
		toastAlert('Failed to Send Frequency');
	});
}

(function() {
	var page = document.getElementById("main"),
	changer = document.getElementById("tabsectionchanger"),
	sectionChanger;
	
	window.addEventListener('tizenhwkey', function(e) {
		/* For the flick down gesture */
		if (e.keyName == "back") {
			var page = document.getElementsByClassName('ui-page-active')[0],
				pageid = page ? page.id : " ";
			if (pageid === "main"){
		         /* When a user flicks down, the application exits */
				tizen.application.getCurrentApplication().exit();
			}
			else {
				window.history.back();
			}
		}
	});
	
	window.addEventListener('load', function(ev) {
		sectionChanger = new tau.widget.SectionChanger(changer, {
			circular: false,
			orientation: "horizontal",
			scrollbar: "tab"
		});
		
		initialize();
	});
}());

(function(ui) {
	var closePopup = ui.closePopup.bind(ui);
	var toastPopup = document.getElementById('popupToast');
	toastPopup.addEventListener('popupshow', function(ev){
		setTimeout(closePopup, 3000);
	}, false);
})(window.tau);
