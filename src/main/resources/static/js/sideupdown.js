$(document).ready(function(){
    var side_hw = 0;
    var side_sw = 0;
    var side_etc = 0;
    var side_option= 0;

    var side = new Array("side_hw","side_sw","side_etc","side_option");
    var sideNum = new Array("0","0","0","0");

    $("#lnb ul li").click(function(){
        var tab = $(this).attr("class");
        var tab2 = "";
        var ck = $("#lnb ul li."+ tab +" ul li.on").attr("class");
        var ck2='';
        var ch = $("#lnb ul li."+ tab +" span.on").attr("class"); 
        var ch2= '';
        console.log(ch);
        if(tab != undefined){
            tab2 = tab.split(" "); 
        }

        if(ck != undefined){
            ck2 = ck.split(" ");
           for(var a= 0; a < ck2.length ; a++){
                if(ck2[a] == "on"){
                    ck = ck2[a];
                }
             }
        }

        if(ch != undefined){
            console.log(ch);
            ch2 = ch.split(" ");
            for(var a = 0; a<ch2.length ;a++){
                if(ch2[a] == "on"){
                    ch = ch2[a];
                }
            }
            console.log(ch);
        }

        for(var i =0; i<side.length ; i++){
            if(side[i] == tab2[0]){
                if(tab2[1] == "on"){
                    sideNum[i] ="1";
                }

                if(sideNum[i] == "0"){
                    $(this).addClass("on");

                    if(tab2[0] =="side_sw"){
                        if( ch != "on"){
                            $("."+ tab2[0]).attr('style',"background-color:#2B3239 !important");
                            $("#lnb ul li."+ tab2[0] +" span button").attr('style',"background-color:#2B3239 !important");	
                        }
                    }else {
                        if(ck != "on"){
                            $("."+ tab2[0]).attr('style',"background-color:#2B3239 !important");
                            $("#lnb ul li."+ tab2[0] +" span button").attr('style',"background-color:#2B3239 !important");	
                        }   
                    }       
                    sideNum[i] = "1";
                }else {
                    $("."+ tab2[0]).attr('style',"color:#B0B0B6 !important");
                    $(this).removeClass("on");
                    sideNum[i] = "0";
                }
            }

        }
    });
});