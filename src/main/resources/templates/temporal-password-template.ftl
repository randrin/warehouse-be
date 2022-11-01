<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>WareHouse System New User Verification Email</title>
    <link href="/css/template-style.css" rel="stylesheet" />
</head>

<body>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td align="center" valign="top" bgcolor="#e0e0e1"
            style="background-color: #e0e0e1;"><br/> <br/>
            <table width="600" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td valign="top" bgcolor="#ffffff"
                        style="background-color: #ffffff; font-family: Arial, Helvetica, sans-serif; font-size: 13px; color: #000000; padding: 0px 15px 10px 15px;">
                        <br />
                        <div align="center" style="font-size: 32px; color:blue;">
                            <b>Warehouse System</b>
                        </div>
                        <div style="font-size: 18px; color: #555100;">
                            <br/> Hello ${name} (Matriculate: ${userId})<br/>
                        </div>
                        <br/>One of our administrator added you in our System. To continue use our products, use your temporal password for log in
                        <br/><br/>
                        <div align="center">
                            Your temporal password: <strong>${temporalPassword}</strong>
                            <br/>
                        </div>
                        <div>
                            <p> Or if your have any problems, please contact our assistance support</p>
                            <br/>
                            <span>Beyond that, the link to <a target="_blank" href="http://localhost:4200/warehouse/login">Warehouse System</a> website.</span>
                            <br/>
                            <p>Thank you for your confidence.</p>
                            <p>Warehouse System</p>
                            <br/>
                        </div>
                    </td>
                </tr>
            </table>
            <br/>
        </td>
    </tr>
</table>
</body>
</html>