<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>New Comment Reply</title>
    <style>
        /* Add your custom styling here */
        body {
            font-family: Arial, sans-serif;
            line-height: 1.6;
            background-color: #f6f6f6;
            margin: 0;
            padding: 20px;
        }
        .container {
            max-width: 600px;
            margin: 0 auto;
            background-color: #ffffff;
            padding: 20px;
            border-radius: 4px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        h1 {
            font-size: 24px;
            color: #333333;
            margin: 0 0 20px;
        }
        p {
            margin: 0 0 10px;
        }
        a {
            color: #2196F3;
            text-decoration: none;
        }
        .comment {
            margin-bottom: 20px;
            padding: 10px;
            border-radius: 4px;
            background-color: #f9f9f9;
        }
        .comment p {
            margin: 0;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Someone replied to your comment.</h1>

    <p>Hi ${username},</p>

    <p>Someone has replied to your comment on ${post_title}. Here are the details:</p>

    <div class="comment">
        <p><strong>Comment:</strong> ${user_comment}</p>
        <p><strong>Reply:</strong> ${reply}</p>
    </div>

    <p>You can view the conversation and reply directly on the platform by ${thread_name}.</p>

    <p>Thank you for using our messaging service!</p>

    <p>Best regards,</p>
    <p>RapidComments</p>
</div>
</body>
</html>
