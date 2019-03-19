from django.contrib import admin

# Register your models here.
from django.contrib import admin
from django.contrib.auth.admin import UserAdmin
from .models import User, Order, Drone, Profile,Store,Product
from django.contrib.auth.admin import UserAdmin as BaseUserAdmin


@admin.register(Profile)
class ProfileAdmin(admin.ModelAdmin):
    list_display = ['user','bio','location','birth_date']


@admin.register(Order)
class OrderAdmin(admin.ModelAdmin):
    list_display = ['orderName', 'id']

@admin.register(Drone)
class DroneAdmin(admin.ModelAdmin):
    list_display = ['droneName', 'id']

# @admin.register(UserOrders)
# class UserOrdersAdmin(admin.ModelAdmin):
#     list_display = ['user', 'order']
@admin.register(Product)
class ProfileAdmin(admin.ModelAdmin):
    list_display = ['myproduct','price','quantity']

@admin.register(Store)
class ProfileAdmin(admin.ModelAdmin):
    list_display = ['mystore']