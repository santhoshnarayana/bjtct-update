$(document).ready(function() {
    console.log("loading aviators .......");

    //gets the record for given teamMember
    var getNextRecord = function(teamId, teamMemberId) {
        $.ajax({
            method: 'GET',
            url: "getNextTeamMember.sec",
            contentType: "application/json; charset=utf-8",
            data: {
                teamId: teamId,
                teamMemberId: teamMemberId
            },
            success: function(data) {
                var data = jQuery.parseJSON(data);

                if (data.teamId !== null && data.mName !== null) {
                    $('#headName').text(data.mName + ' ' + data.surName);

                    $('#playersPho').attr('src', 'data:image/png;base64,' + data.photo);
                    $('#pname').text(data.mName);
                    $('#surname').text(data.surName);
                    $('#dob').text(data.dob);
                    $('#age').text(data.age);
                    $('#residential-area').text(data.recidence);
                    $('#profesion').text(data.profession);
                    $('#merital-status').text(data.meritalStatus);
                    $('#aniversary').text(data.aniversary);
                    $('#blood-group').text(data.bloodGroup);
                    $('#social-involvement').text(data.socialInvolveMent);
                    $('#contact').text(data.contact);
                    //console.log(data.mName);
                    return true;
                } else {
                    return false;
                }

            }

        });
    };

    var teamId = $('#teamId').val();
    var record = $('#firstRecordId').val();
    var count = 1;
    getNextRecord(teamId, record);

    $('#next-record').click(function() {

        $('#prev-record').children().attr('src', '../images/prev.png');
        console.log('clicked next');
        var maxRecords = $('#totalRecordCount').val();
        var teamId = $('#teamId').val();
        if (count < maxRecords) {
            $(this).children().attr('src', '../images/nxt.png');
            record++;
            count++;
            getNextRecord(teamId, record);
        } else {
            //todo image dim for maxrecords reached
            $(this).children().attr('src', '../images/nxtdimm.png');
        }
    });

    $('#prev-record').click(function() {
        $('#next-record').children().attr('src', '../images/nxt.png');
        var teamId = $('#teamId').val();
        if (count != 1) {
            $(this).children().attr('src', '../images/prev.png');
            count--;
            record--;
            getNextRecord(teamId, record);
        } else {
            $(this).children().attr('src', '../images/prevdimm.png');
            //todo hide image
        }

    });



    /**
     * Team statistics page scripts
     * stops upto brk point if brk 0 means all records shows
     */

    var buildRankingTable = function(tabName, brk) {
        var TABLE_TBODY = $('#ratings-body');
        var LAST_ROW = '<tr><td id="all-team-ranks" colspan="3" align="right" style="border-top: 1px solid #ffd419;">Click here to see all Teams</td></tr>';
        TABLE_TBODY.html('');

        $.ajax({
            method: 'GET',
            url: "../team/getRatings.sec",
            contentType: "application/json; charset=utf-8",
            data: {
                tabName: tabName
            },
            success: function(data) {
                var data = jQuery.parseJSON(data);
                console.log(data);
                for (var _i = 0; _i < data.length; _i++) {
                    var row = '<tr><td align="center">' + data[_i].rank + '</td><td>' + data[_i].teamName + '</td><td align="center">' + data[_i].rating + '</td></tr>';
                    TABLE_TBODY.append(row);
                    if (brk != 0 && _i == brk - 1) {
                        TABLE_TBODY.append(LAST_ROW);
                        $('#all-team-ranks').click(function() {
                            buildRankingTable(tabName, 0);
                        });

                        break;
                    }

                }




            }
        });
    };

    $('#team-statsTabLinks').click(function(evnt) {


        var selLi = $(evnt.target);
        var k = selLi.addClass('selected').parent().siblings().children().removeClass('selected');
        console.log(k);
        console.log("[INFO] clicked on:" + selLi.text());

        if (selLi !== undefined) {
            buildRankingTable(selLi.text(), 5);
        }




    });

    var buildFlixtureOrResults = function(tabName, brk) {
        var DIV_ID = $('#fixtures-news');
        var DIV_ID_RESULTS = $('#fixture-results');

        DIV_ID.html('');
        DIV_ID_RESULTS.html('');

        if (tabName == 'Fixtures') {
            $.ajax({
                method: 'GET',
                url: "../team/getSeasonAtGlance.sec",
                contentType: "application/json; charset=utf-8",
                data: {
                    tabName: tabName
                },
                success: function(data) {
                    var data = jQuery.parseJSON(data);
                    console.log(data);
                    for (var _i = 0; _i < data.length; _i++) {
                        var datap = '<p><b>' + data[_i].leftTeamName + ' Vs ' + data[_i].rightTeamName + '</b> at ' + data[_i].venue + '<span>' + data[_i].date + '</span> </p>';


                        DIV_ID.append(datap);

                    }
                }
            });
        } else if(tabName == 'Results') {
            
        $.ajax({
                method: 'GET',
                url: "../team/getSeasonAtGlance.sec",
                contentType: "application/json; charset=utf-8",
                data: {
                    tabName: tabName
                },
                success: function(data) {
                    var data = jQuery.parseJSON(data);
                    console.log(data);
                    for (var _i = 0; _i < data.length; _i++) {
                        var datap = '<p><b>' + data[_i].leftTeamName + ' Vs ' + data[_i].rightTeamName + '</b>  <span>' + data[_i].winningTeamName +' won by '+data[_i].wonBy+'</span> </p>';


                        DIV_ID.append(datap);

                    }
                }
            });

        
        }
        


    };


    $('#team-season-glance').click(function(evnt) {

        var selLi = $(evnt.target);
        var k = selLi.addClass('selected').parent().siblings().children().removeClass('selected');
        console.log(k);
        console.log("[INFO] clicked on:" + selLi.text());

        if (selLi !== undefined) {
            buildFlixtureOrResults(selLi.text(), 5);
        }
    });
    
    /**
     * For get Latest News
     */
    var buildLatestNews = function(tabName,brk){
    	var TABLE_TBODY = $('#latest-news');
        var LAST_ROW = '<tr><td id="all-team-ranks" colspan="3" align="right" style="border-top: 1px solid #ffd419;">Click here to see all Teams</td></tr>';
        TABLE_TBODY.html('');

        $.ajax({
            method: 'GET',
            url: "../team/getLatestNews.sec",
            contentType: "application/json; charset=utf-8",
            data: {
                tabName: tabName
            },
            success: function(data) {
                var data = jQuery.parseJSON(data);
                console.log(data);
                for (var _i = 0; _i < data.length; _i++) {
                    var row = '<p>'+data[_i].news+'</p>';
                    TABLE_TBODY.append(row);
                    if (brk != 0 && _i == brk - 1) {
                        TABLE_TBODY.append(LAST_ROW);
                        $('#all-team-ranks').click(function() {
                            buildRankingTable(tabName, 0);
                        });

                        break;
                    }

                }




            }
        });
        
    };
   
        /**
     * Team statistics page scripts For pool information
     * stops upto brk point if brk 0 means all records shows
     */

    var buildPoolTable = function(tabName,brk){
        var TABLE_TBODY = $('#group-pool-body');
        var LAST_ROW = '<tr><td id="all-pool-members" colspan="3" align="right" style="border-top: 1px solid #ffd419;">Click here to see all Teams</td></tr>';
        TABLE_TBODY.html('');

        $.ajax({
            method: 'GET',
            url: "../team/getPoolInfo.sec",
            contentType: "application/json; charset=utf-8",
            data: {
            	poolTabName: tabName
            },
            success: function(data) {
                var data = jQuery.parseJSON(data);
                console.log(data);
                for (var _i = 0; _i < data.length; _i++) {
                    var row = '<tr><td align="center">' + (_i+1) + '</td><td>' + data[_i].teamName + '</td><td align="center">' + data[_i].rr + '</td></tr>';
                    TABLE_TBODY.append(row);
                    if (brk != 0 && _i == brk - 1) {
                        TABLE_TBODY.append(LAST_ROW);
                        $('#all-pool-members').click(function() {
                        	buildPoolTable(tabName, 0);
                        });

                        break;
                    }

                }




            }
        });
    };
    
  /**
   * Get the pool info on click
   */

    $('#group-pool').click(function(evnt){
    	var selLi = $(evnt.target);
        var k = selLi.addClass('selected').parent().siblings().children().removeClass('selected');
        console.log(k);
        console.log("[INFO] clicked on:" + selLi.text());

        if (selLi !== undefined) {
        	buildPoolTable(selLi.text(),5);
        }

        
    });//pool code ends here
    
    
    /**
     * General Statistics table generation
     */
	
	var  buildGeneralStatisticsTable = function(tabName,brk) {
    	var TABLE_TBODY = $('#general-stats-body');
        var LAST_ROW = '<tr><td id="all-team-ranks" colspan="3" align="right" style="border-top: 1px solid #ffd419;">Click here to see all Teams</td></tr>';
        TABLE_TBODY.html('');

        $.ajax({
            method: 'GET',
            url: "../team/getGeneralStats.sec",
            contentType: "application/json; charset=utf-8",
            data: {
                tabName: tabName
            },
            success: function(data) {
                var data = jQuery.parseJSON(data);
                console.log(data);
                for (var _i = 0; _i < data.length; _i++) {
                    var row = '<tr><td>'+data[_i].teamName+'</td><td>'+data[_i].played+'</td><td>'+data[_i].won+'</td><td>'+data[_i].loss+'</td><td>'+data[_i].draw+'</td><td>'+data[_i].points+'</td></tr>';
                    TABLE_TBODY.append(row);
                    if (brk != 0 && _i == brk - 1) {
                        TABLE_TBODY.append(LAST_ROW);
                        $('#all-team-ranks').click(function() {
                            buildRankingTable(tabName, 0);
                        });

                        break;
                    }

                }
            }
        });
        
    }; //general statistics code end
	
    
	
	
	/**
	 * getBest players info
	 */
	var buildBestPlayersTable = function(playerType,brk) {
		
	 var BATS_MAN_TABLE = $('#best-batsmen');
        var BOWLERS_TABLE = $('#best-bowlers');
        var BATS_MAN_TABLE_TBODY = $('#best-batsmen tbody');
        var BOWLERS_TABLE_TBODY = $('#best-bowlers tbody');
        var LAST_ROW = '<tr><td id="all-team-ranks" colspan="3" align="right" style="border-top: 1px solid #ffd419;">Click here to see all Teams</td></tr>';
        BATS_MAN_TABLE_TBODY.html('');
        BOWLERS_TABLE_TBODY.html('');
		
	 if (playerType === 'Bowler') {
        	console.log("[INFO] inside bowlers table...");
        	BATS_MAN_TABLE.hide();
        	BOWLERS_TABLE.show();
        	$.ajax({
                method: 'GET',
                url: "../team/getBestPlayersInfo.sec",
                contentType: "application/json; charset=utf-8",
                data: {
                	playerType: playerType
                },
                success: function(data) {
                    var data = jQuery.parseJSON(data);
                    console.log(data);
                    for (var _i = 0; _i < data.length; _i++) {
                        var row = '<tr><td>'+data[_i].teamMemberName+'</td><td>'+data[_i].runs+'</td><td>'+data[_i].matches+'</td><td>'+data[_i].wickets+'</td><td>'+data[_i].teamName+'</td></tr>';
                        BOWLERS_TABLE_TBODY.append(row);
                    }
                }
            });
        	

        	
        } else if(playerType === 'Batsmen') {
        	console.log("[INFO] inside batsmen table...");
        	BATS_MAN_TABLE.show();
        	BOWLERS_TABLE.hide();
        	$.ajax({
                method: 'GET',
                url: "../team/getBestPlayersInfo.sec",
                contentType: "application/json; charset=utf-8",
                data: {
                	playerType: playerType
                },
                success: function(data) {
                    var data = jQuery.parseJSON(data);
                    console.log(data);
                    for (var _i = 0; _i < data.length; _i++) {
                        var row = '<tr><td>'+data[_i].teamMemberName+'</td><td>'+data[_i].runs+'</td><td>'+data[_i].matches+'</td><td>'+data[_i].hs+'</td><td>'+data[_i].teamName+'</td></tr>';
                        BATS_MAN_TABLE_TBODY.append(row);
                    }
                }
            }); 
        }
  
	
	}
	
	
    /**
   * Get the bestplayers info on click
   */

    $('#best-players').click(function(evnt){
    	
       

        
    	var selLi = $(evnt.target);
        var k = selLi.addClass('selected').parent().siblings().children().removeClass('selected');
        console.log(k);
        console.log("[INFO] clicked on:" + selLi.text());
        var playerType = selLi.text();
             
        buildBestPlayersTable(playerType,0);
                        
    });//bestplayers code ends here


    buildBestPlayersTable('Batsmen',0);//onLoad calll
    buildPoolTable('Pool A',5); //onload call
    buildRankingTable('Team', 5);//onload call
    buildFlixtureOrResults('Fixtures', 5);//onload call
    buildLatestNews('news',5);//onload call
    buildGeneralStatisticsTable('general',5);
    


    //team statisticks page scripts end 
});