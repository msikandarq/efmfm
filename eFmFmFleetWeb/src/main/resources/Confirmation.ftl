<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<style>
* {font-family: Helvetica Neue, Arial, sans-serif; }

body {background: white-smoke;
}

h2, table { text-align: center; }
h3, table { text-align: center; }

table {border-collapse: collapse;  width: 90%; margin: 0 auto 0.5rem;}

th, td { padding: 1.5rem; font-size: 1.3rem; }

tr {background: hsl(50, 50%, 80%); }

tr, td { transition: .4s ease-in; }

tr:first-child {    background-image: linear-gradient(#00BCD4 25%, #82e6a1); }

tr:nth-child(even) { background: hsla(50, 50%, 80%, 0.7); }

td:empty {background: hsla(50, 25%, 60%, 0.7); }

tr:hover:not(#firstrow), tr:hover td:empty {background: #ff0; pointer-events: visible;}
tr:hover:not(#firstrow) { transform: scale(1.2); font-weight: 700; box-shadow: 0px 3px 7px rgba(0, 0, 0, 0.5);}

</style>
</head>
<body>
<table style="width:90%; min-width: 880px;">
<tr id="firstrow"><th style="text-align: left;"><img src="/images/OcwenLogo.png" style="width:100px; height:100px;"></img></th>
	<th style="text-align:center; vertical-align: middle;">Employees Feed back Summary</th>
	<th style="text-align:right; vertical-align: middle;">${feedBackDateTime}</th>
</tr>
</table>

<div style="overflow-x:auto;">
<table id="racetimes" style="width:90%; min-width: 880px;">
<tr id="firstrow"><th>Time</th>
	<th>Employee Name</th>
	<th>Employee Email ID</th>
	<th>Category</th>
	<th>Subject</th>
	<th>Details</th>
</tr>

<tr><td>${feedBackDateTime}</td>
	<td>${empName}</td>
	<td>${empEmailId}</td>
	<td>${category}</td>
	<td>${subject}</td>
	<td>${empDescription}</td></tr>

</table>
</div>
<table style="width:90%; min-width: 880px;">
<tr id="firstrow">
	<td style="text-align: left; font-size: 12px; ">This Email link was sent to you by <a href="http://www.ocwen.com/" style=" color: white;">Ocwen Helping Homeowners Is What We Do!®</a> </td>
	<td style="text-align: right; margin-top: -20px; font-size: 12px;">Copyright @ 2017 © Ocwen Financial Corporation. All Rights Reserved. </td>
</tr>
</table>
</body>
</html>
