from django.contrib.auth.models import User, Group
from rest_framework import serializers
from .models import Profile, Store ,Drone, Product,Order
from collections import OrderedDict

class UserSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = User
        fields = ('url', 'username', 'email', 'groups')


class GroupSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Group
        fields = ('url', 'name')

class OrderSerializer(serializers.ModelSerializer):
    # ##order = serializers.StringRelatedField(many=False)
    #
    class Meta:
        model = Order
        fields = '__all__'

class OrderStatusSerializer(serializers.ModelSerializer):
    # ##order = serializers.StringRelatedField(many=False)
    #
    class Meta:
        model = Order
        fields = ('orderName', 'status')
class OrderSerializerNull(serializers.ModelSerializer):
    # ##order = serializers.StringRelatedField(many=False)
    #
    class Meta:
        model = Order
        fields = '__all__'

    def to_representation(self, value):
        repr_dict = super(OrderSerializerNull, self).to_representation(value)
        return OrderedDict((k, v) for k, v in repr_dict.items()
                           if v not in [None, [], '', {}, 0])

class ProductSerializer(serializers.ModelSerializer):
    product = serializers.StringRelatedField(many=False)

    class Meta:
        # model = Profile
        model = Product
        fields = '__all__'#('myproduct','price','description','quantity')

class DroneSerializer(serializers.ModelSerializer):


    class Meta:
        model = Drone
        fields = '__all__'

class StoreSerializer(serializers.ModelSerializer):
    # storeProduct = serializers.StringRelatedField(many=False)
    storeProduct = ProductSerializer(many=True)

    class Meta:
        model = Store
        fields = '__all__'
# class UserOrdersSerializer(serializers.ModelSerializer):
#     class Meta:
#         model = UserOrders
#         fields = '__all__'


class ProfileSerializer(serializers.ModelSerializer):
    user = serializers.StringRelatedField(many=False)
    order = serializers.StringRelatedField(many=False)


    class Meta:
        model = Profile
        fields = '__all__'

class MyDroneSerializer(serializers.ModelSerializer):


    class Meta:
        model = Drone
        fields = ('currentCoord',)

