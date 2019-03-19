from django.contrib import admin

# Register your models here.
from django.contrib import admin
from django.contrib.auth.admin import UserAdmin
from .models import User, Order, Drone
from django.contrib.auth.admin import UserAdmin as BaseUserAdmin



admin.site.register(User, UserAdmin)
class MyUserAdmin(admin.ModelAdmin):
    # list_display = ['username', 'group_info', 'email', 'first_name', 'last_name', 'is_active', 'last_login']
    #
    # def group_info(self, obj):
    #     return ','.join([g.name for g in obj.groups.all()]) if obj.groups.count() else ''

    pass
    # Lines to Add
    #ordering = ['username', 'email', 'first_name', 'last_name', 'is_active', 'last_login']

    # Lines to Add
    # exclude = ('is_superuser',)

# To get back extended User Admin, uncomment below

# admin.site.unregister(User)
# admin.site.register(User, MyUserAdmin)


@admin.register(Order)
class OrderAdmin(admin.ModelAdmin):
    list_display = ['orderName', 'id']

@admin.register(Drone)
class DroneAdmin(admin.ModelAdmin):
    list_display = ['droneName', 'id']