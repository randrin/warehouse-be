<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>Java Techie Mail</title>
    <link href="/css/template-style.css" rel="stylesheet" />
</head>

<body>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td align="center" valign="top" bgcolor="#838383"
            style="background-color: #838383;"><br/> <br/>
            <table width="600" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td valign="top" bgcolor="#d3be6c"
                        style="background-color: #d3be6c; font-family: Arial, Helvetica, sans-serif; font-size: 13px; color: #000000; padding: 0px 15px 10px 15px;">
                        <br />
                        <div align="center" style="font-size: 32px; color:blue;">
                            <b>Warehouse System</b>
                        </div>
                        <div style="font-size: 18px; color: #555100;">
                            <br/> Hello ${name} (Name: ${name})<br/>
                        </div>
                        <br/>To verify your email, please click on the button below
                        <br/><br/>
                        <div align="center">
                            <a href="http://localhost:4200/warehouse/noAuth/resetPassword?idLinkResetPassword=${link}&amp;expirationLink=${expirationLink}&amp;verifyType=${verifyType}"
                            style="        position: relative;
        font-weight: 400;
        white-space: nowrap;
        text-align: center;
        border: 1px solid #d9d9d9;
        box-shadow: 0 2px 0 rgb(0 0 0 / 2%);
        cursor: pointer;
        transition: all .3s cubic-bezier(.645,.045,.355,1);
        -webkit-user-select: none;
        user-select: none;
        height: 32px;
        padding: 4px 15px;
        border-radius: 2px;
        background: #fff;         color: #fff;
        border-color: #1890ff;
        background: #1890ff;
        text-decoration: none;
        text-shadow: 0 -1px 0 rgb(0 0 0 / 12%);
        box-shadow: 0 2px 0 rgb(0 0 0 / 5%);" target="_blank">
                               Reset your password
                            </a>
                            <br/>
                        </div>
                        <div>
                            <p> Or if your have any problems, please click on the link below (or copy it into your browser)</p>
                            <p>http://localhost:4200/warehouse/noAuth/resetPassword?idLinkResetPassword=${link}&amp;expirationLink=${expirationLink}&amp;verifyType=${verifyType}</p>
                            <br/>
                            <span><b>Attention, this link is valid for a period of 15 minutes.</b> Beyond that, you will have to start the procedure again on the <a target="_blank" href="http://localhost:4201/warehouse/login">Warehouse System</a> website.</span>
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