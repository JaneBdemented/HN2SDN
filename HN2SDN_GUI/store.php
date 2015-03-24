<?php
require 'core/init.php';


//Retrive New rules to be stored
$info = $_REQUEST['Update'];
$value = explode(",",$info[0]);

/*
*	Get required value form DB for calculations (bw_total)
*/
$getNetnum = DB::getInstance()->query("SELECT total_bw, total_data FROM macRulesTable");
foreach ($getNetnum->results() as $get) {
	$BW = $get->total_bw;
	$Data = $get->total_data;	
}

/*
* Create Database friendly arrays from rules
*/
$usr = Array();
$block = Array();
$DL = Array();
$st = Array();
$endt = Array();
$speed = Array();
$date = new DateTime();
for ($i = 0; $i<count($value);$i++){
	$usr[$i] = $value[$i];
	if($value[$i] =='Denied'){
		$block[$i] = 1;
	}else{
		$block[$i] = 0;
	}
	if($value[$i+2]=='No Limit'){
		$DL[$i] = $Data;
	}else if($value[$i+3]=='MB'){
		$DL[$i] = ($value[$i+2])*1000000;
	}else{
		$DL[$i] = ($value[$i+2])*1000000000;
	}
	if($value[$i+4]=='No Limit'){
		$st[$i] = "00:00:00";
	}else if($value[$i+5]='AM'){
		$t1 = explode(':',$value[$i+4]);
		$st[$i] = $value[$i+4];
	}else{
		$t1 = explode(':',$value[$i+4]);
		$h = ($t1)+12;
		$st[$i] = " ".$h.":".$t1[1].":".$t1[2]." "; //this will not work but for now
	}
	if($value[$i+6]=='No Limit'){
		$endt[$i] = "00:00:00";
	}else if($value[$i+7]=='AM'){
		$t2 = explode(':',$value[$i+6]);
		$endt[$i] = $value[$i+6];
	}else{
		$t2 = explode(':',$value[$i+6]);
		$h2 = ($t2[0])+12;
		$endt[$i] = " ".$h2.":".$t2[1].":".$t2[2]." "; //this will not work but for now
	}
	switch($value[$i+8]){
		case('No Limit'):	$speed[$i] = $BW;
							break;
		case('Fast'):		$speed[$i] = ($BW)*0.5;
							break;
		case('Medium'):		$speed[$i] = ($BW)*0.25;
							break;				
		case('Slow'):		$speed[$i] = ($BW)*0.1;
							break;				
	}
	$i=$i+8;
}


/*
*	Opening Connection to DataBase to store new Rules
*
*/
$conn = new mysqli('localhost', 'root', '', 'macRulesSchema');
	// Check connection
	if ($conn->connect_error) {
		echo("Connection failed: " . $conn->connect_error);
	}
for($i=0;$i<count($usr);$i++){
	$fields= Array('block'=>$block[$i], 'user_total'=>$DL[$i], 'start_time'=>$st[$i], 'stop_time'=>$endt[$i], 'bw_limit'=>$speed[$i]);
	$where = $usr[$i];
	$sql = "UPDATE macRulesTable SET `block`={$fields['block']}, `user_total`={$fields['user_total']}, `start_time`='{$fields['start_time']}', `stop_time`='{$fields['stop_time']}', `bw_limit`={$fields['bw_limit']}  WHERE terminal_name = '{$where}'";
	$test = $conn->query($sql);
	if($test){
		echo $test;
	}else{
		echo '-->PASS<--';
	}
	}
$conn->close();



?>