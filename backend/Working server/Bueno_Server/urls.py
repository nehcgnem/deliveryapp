"""Bueno_Server URL Configuration

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
from django.urls import path
from django.conf.urls import url, include
from allauth.account.views import confirm_email
from User_Requests import views
from rest_framework import routers
from User_Requests.views import CustomObtainAuthToken
from django.conf import settings
from django.conf.urls.static import static


urlpatterns = [
    path('admin/', admin.site.urls),
    # path('user/', include(router.urls)),
    # url('drone/', views.DroneViewSet.as_view()),
    url(r'^rest-auth/login', CustomObtainAuthToken.as_view()),
    url('createorder/(?P<username>\w+)/$', views.UserOrdersViewSet.as_view()),
    url('product/(?P<mystore>\w+)/(?P<myproduct>\w+)/$', views.ProductViewSet.as_view()),
    url('allstores/', views.AllStoresViewSet.as_view()),
    url('store/(?P<mystore>\w+)/$', views.StoreViewSet.as_view()),
    url('orderstatus/(?P<username>\w+)/(?P<ordername>\w+)/$', views.UserOrdersStatusViewSet.as_view()),
    url('drone-coord/(?P<username>\w+)/(?P<ordername>\w+)/$', views.MyDroneViewSet.as_view()),
    url('confirm-order/(?P<username>\w+)/(?P<ordername>\w+)/$', views.ConfirmOrderViewSet.as_view()),
    url('order/(?P<username>\w+)/$', views.AllOrderViewSet),
    url('profile/(?P<username>\w+)/$', views.ProfileViewSet.as_view()),
    # url(r'^accounts/', include('allauth.urls')),
    url('payment/(?P<username>\w+)/(?P<ordername>\w+)/$', views.PaymentViewSet.as_view()),
    url('storeuser/(?P<username>\w+)/$', views.MyStoreViewSet.as_view()),
    url(r'^rest-auth/registration/', include('rest_auth.registration.urls')),
    # url(r'^accounts/confirm-email/(?P<key>.+)/$', confirm_email, name='account_confirm_email'),
    # path('email_confirmed/',views.index, name='index'),
    url(r'^api-auth/', include('rest_framework.urls', namespace='rest_framework'))
]

if settings.DEBUG:
    urlpatterns += static(settings.STATIC_URL, document_root=settings.STATIC_ROOT)
    urlpatterns += static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)