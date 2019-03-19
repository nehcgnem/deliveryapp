"""Bueno URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/2.1/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.contrib import admin
from django.urls import path, include
from django.conf.urls import url, include
from rest_framework.urlpatterns import format_suffix_patterns
from rest_framework import routers
from BuenoApp.views import OrderViewSet
from BuenoApp import views


from rest_auth.registration.views import VerifyEmailView, RegisterView, LoginView


router = routers.DefaultRouter()
router.register(r'Order',OrderViewSet)

urlpatterns = [
    path('admin/', admin.site.urls),

    # NEW BELOW
    # url(r'^accounts/', include('allauth.urls')),
    # NEW ABOVE
    # path('accounts/', include ('allauth.urls')),

    # url(r'^accounts/', include('django_registration.backends.one_step.urls')),
    # url(r'^accounts/', include('django.contrib.auth.urls')),


    # path('accounts/', include ('registration.backends.simple.urls')),

    # Login etc.
    # path('', views.OrderList.as_view()),
    # path('<int:pk>/', views.OrderDetail.as_view()),

    # =============================================================================
    # path('rest-auth/', include('rest_auth.urls')),
    # #
    # path('rest-auth/registration/', include('rest_auth.registration.urls')),

    # path('users/', include('users.urls')),
    path('rest-auth/', include('rest_auth.urls')),


    path('rest-auth/registration/', include('rest_auth.registration.urls')),
    # =============================================================================

# =============================================================================
#     path('', include('rest_auth.urls')),
#     path('registration/', include('rest_auth.urls')),


    # path('login/', LoginView.as_view(), name='account_login'),
    # # path('registration/', include('rest_auth.registration.urls')),
    # path('registration/', RegisterView.as_view(), name='account_signup'),
    #
    # url(r'^account-confirm-email/', VerifyEmailView.as_view(),
    #      name='account_email_verification_sent'),
    # # Problem is here
    # url(r'^account-confirm-email/(?P<key>[-:\w]+)/$', VerifyEmailView.as_view(),
    #      name='account_confirm_email'),
# =============================================================================


    # REVIVE ME LATER
    # url(r'^', include('django.contrib.auth.urls')),

    path('orders/', views.OrderList.as_view()),
    url(r'^orders/(?P<pk>[0-9]+)/', views.OrderDetail.as_view()),

    path('users/', views.UserListView.as_view()),

    # path('signupreq/', views.Process_Signup_Request.as_view()),

    url(r'^', include(router.urls)),
]

# urlpatterns = format_suffix_patterns(urlpatterns)

