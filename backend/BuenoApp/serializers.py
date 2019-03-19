from rest_framework import serializers
from .models import Order, Drone, User

class OrderSerializer(serializers.ModelSerializer):

    class Meta:
        model = Order
        fields = ('id','orderName',) # add more as required OR field = '__all__'
        # fields = '__all__'

# class SignUp_Request_Serializer(serializers.ModelSerializer):
#
#     class Meta:
#         model = User
#         fields = '__all__'

class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = ('email', 'username', )