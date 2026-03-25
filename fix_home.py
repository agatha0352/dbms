with open(r'c:\Users\USER\sara 2\src\main\resources\templates\home.html', 'w', encoding='utf-8') as f:
    f.write('''<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" xmlns:sec="http://www.thymeleaf.org/extras/spring-security">
<head>
    <title>Home - SARA</title>
    <th:block th:replace="fragments/layout :: head"></th:block>
</head>
<body>
<th:block th:replace="fragments/layout :: navbar"></th:block>
<th:block th:replace="fragments/layout :: alerts"></th:block>

<section class="hero">
    <div class="hero-content">
        <div class="container">
            <h1><i class="bi bi-heart-fill me-3"></i>Smart Aid Resource Assistant</h1>
            <p class="lead">Connecting hearts when disasters strike.</p>
            <p class="lead">Helping communities find hope, resources, and support in times of crisis.</p>
        </div>
    </div>
</section>

<section class="section-padding bg-light">
    <div class="container">
        <div class="row g-4">
            <div class="col-md-4">
                <div class="cta-card">
                    <div class="icon">
                        <i class="bi bi-hand-thumbs-up-fill"></i>
                    </div>
                    <h3>Get Help</h3>
                    <p>Find essential resources and support during emergencies.</p>
                    <a th:href="@{/resources}" class="btn btn-primary">Browse Resources</a>
                </div>
            </div>
            
            <div class="col-md-4">
                <div class="cta-card">
                    <div class="icon">
                        <i class="bi bi-gift-fill"></i>
                    </div>
                    <h3>Donate</h3>
                    <p>Share resources with those in need.</p>
                    <a th:href="@{/register}" class="btn btn-success">Start Donating</a>
                </div>
            </div>
            
            <div class="col-md-4">
                <div class="cta-card">
                    <div class="icon">
                        <i class="bi bi-people-fill"></i>
                    </div>
                    <h3>Volunteer</h3>
                    <p>Join our community of helpers.</p>
                    <a th:href="@{/register}" class="btn btn-primary">Join Us</a>
                </div>
            </div>
        </div>
    </div>
</section>

<section class="section-padding" sec:authorize="!isAuthenticated()">
    <div class="container text-center">
        <h2 class="mb-4">Ready to Make a Difference?</h2>
        <p class="lead mb-4">Join SARA today and be part of a compassionate community.</p>
        <div class="d-flex gap-3 justify-content-center">
            <a th:href="@{/register}" class="btn btn-success btn-lg">Register Now</a>
            <a th:href="@{/login}" class="btn btn-primary btn-lg">Login</a>
        </div>
    </div>
</section>

<th:block th:replace="fragments/layout :: footer"></th:block>
<th:block th:replace="fragments/layout :: scripts"></th:block>
</body>
</html>''')
print("Home page created successfully!")