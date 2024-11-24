<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title>エラーページ</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/error.css">
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f8f9fa;
            color: #333;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }
        .error-container {
            max-width: 600px;
            padding: 30px;
            background-color: #ffffff;
            border: 1px solid #ddd;
            border-radius: 10px;
            box-shadow: 0px 0px 15px rgba(0, 0, 0, 0.1);
            text-align: center;
        }
        .error-container h1 {
            font-size: 2em;
            color: #e74c3c;
        }
        .error-container p {
            font-size: 1.2em;
            margin: 15px 0;
        }
        .error-container a {
            color: #3498db;
            text-decoration: none;
            font-weight: bold;
        }
        .error-container a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <div class="error-container">
        <h1>エラーが発生しました</h1>
        <p>申し訳ございませんが、リクエストを処理できませんでした。</p>
        <p>お手数ですが、しばらくしてからもう一度お試しください。</p>
        <p>
            <a href="${pageContext.request.contextPath}/employeeList">ホームページに戻る</a>
        </p>
    </div>
</body>
</html>
