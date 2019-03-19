
from django.http import HttpResponse, JsonResponse
from rest_framework.views import APIView
from django.contrib.auth.models import User, Group
from rest_framework import viewsets
from User_Requests.serializers import UserSerializer, GroupSerializer
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status, viewsets
from .models import Order, Profile, Drone, User, Store, Product
from .serializers import UserSerializer, ProfileSerializer, OrderSerializer,StoreSerializer,OrderSerializerNull,DroneSerializer, ProductSerializer, OrderStatusSerializer, MyDroneSerializer#, SignUp_Request_Serializer
from django.http import Http404
from rest_framework import generics
from .tasks import Drone_Function
from rest_framework.authtoken.views import ObtainAuthToken
from rest_framework.authtoken.models import Token
from rest_framework.response import Response
from django.template.defaultfilters import slugify
import stripe
import json
class ProfileViewSet(APIView):
  ##  queryset = Profile.objects.all().order_by('-date_joined')
    def get_object(self, pk):
        try:
            return Profile.objects.get(pk=pk)
        except Profile.DoesNotExist:
            raise Http404

    def get(self, request, username):
        getUser = User.objects.get(username=username)
        getProfile = Profile.objects.get(user=getUser)
        serializer = ProfileSerializer(getProfile)
        return Response(serializer.data)

    def put(self, request,username):
        getUser = User.objects.get(username=username)
        snippet = Profile.objects.get(user=getUser)
        serializer = ProfileSerializer(snippet, data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    def delete(self, request, pk):
        snippet = self.get_object(pk)
        snippet.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)


class StoreViewSet(APIView):
  ##  queryset = Profile.objects.all().order_by('-date_joined')
    def get_object(self, pk):
        try:
            return Store.objects.get(pk=pk)
        except Store.DoesNotExist:
            raise Http404

    def get(self, request, mystore):
        # getUser = Product.objects.get(myproduct=myproduct)
        getStore = Store.objects.get(mystore=mystore)#(pk=getUser.pk)
        serializer = StoreSerializer(getStore)
        return Response(serializer.data)

    def put(self, request, pk):
        snippet = self.get_object(pk)
        serializer = StoreSerializer(snippet, data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    def delete(self, request, pk):
        snippet = self.get_object(pk)
        snippet.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)



class ProductViewSet(APIView):
  ##  queryset = Profile.objects.all().order_by('-date_joined')
    def get_object(self, pk):
        try:
            return Product.objects.get(pk=pk)
        except Product.DoesNotExist:
            raise Http404

    def get(self, request, myproduct,mystore):
        getStore = Store.objects.get(mystore=mystore)
        getProduct = Product.objects.all().filter(store=getStore).all().filter(myproduct=myproduct).get()
        serializer = ProductSerializer(getProduct)
        return Response(serializer.data)

    def put(self, request, pk):
        snippet = self.get_object(pk)
        serializer = ProductSerializer(snippet, data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    def delete(self, request, pk):
        snippet = self.get_object(pk)
        snippet.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)


class DroneViewSet(generics.ListCreateAPIView):
    queryset = Drone.objects.all()
    serializer_class = DroneSerializer

class UserOrdersViewSet(APIView):

    # create a new order
    def post(self, request, username):
        getUser = User.objects.get(username=username)
        request.data['user']=getUser.pk
        serializer = OrderSerializer(data=request.data)

        if serializer.is_valid():
            serializer.save()
            orderName = request.data['orderName']
            try:
                getOrder = Order.objects.get(orderName=orderName)
            except:
                data = {
                    'Error': 'Wrong format'
                }
                return JsonResponse(data)

            droneToAssign = Drone.objects.filter(order=None).first()
            if droneToAssign!=None:
                droneToAssign.order=getOrder;
                getProfile = Profile.objects.get(user=getUser)

                droneToAssign.destinationCoord = getProfile.userCoord
                droneToAssign.save()
                droneToAssign.droneName
                Drone_Function(droneToAssign.droneName,orderName,username)
            else:
                getOrder.delete()
                data = {
                    'Error': 'No drones avaliable at the moment'
                }
                return JsonResponse(data)
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    def delete(self, request, pk):
        snippet = self.get_object(pk)
        snippet.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)


class UserViewSet(viewsets.ModelViewSet):
    """
    API endpoint that allows users to be viewed or edited.
    """
    queryset = User.objects.all().order_by('-date_joined')
    serializer_class = UserSerializer


class GroupViewSet(viewsets.ModelViewSet):
    """
    API endpoint that allows groups to be viewed or edited.
    """

    queryset = Group.objects.all()
    serializer_class = GroupSerializer

def index(request):
    return HttpResponse("Email Confirmation Sucessfull.")


def AllOrderViewSet(request, username):

    getUser = User.objects.get(username=username)
    queryset = Order.objects.all().filter(user=getUser)
    serializer = OrderSerializerNull(queryset, many=True)
    return JsonResponse(serializer.data, safe=False)



class AllStoresViewSet(generics.ListCreateAPIView):
    queryset = Store.objects.all()
    serializer_class = StoreSerializer


class CustomObtainAuthToken(ObtainAuthToken):
    def post(self, request, *args, **kwargs):
        response = super(CustomObtainAuthToken, self).post(request, *args, **kwargs)
        user = User.objects.filter(username=request.data['username'])
        getProfile = Profile.objects.get(user=user[0])
        token = Token.objects.get(key=response.data['token'])
        return Response({'token': token.key, 'userType': getProfile.usertype})



class UserOrdersStatusViewSet(APIView):

    def get(self, request, username,ordername):
        getUser = User.objects.get(username=username)
        getOrder = Order.objects.get(user=getUser, orderName=ordername )
        serializer = OrderStatusSerializer(getOrder)
        return Response(serializer.data)



class MyStoreViewSet(APIView):

    def get_object(self, store):
        try:
            return Product.objects.get(store=store)
        except Product.DoesNotExist:
            raise Http404


    def get(self, request, username):
        getUser = User.objects.get(username=username)
        getProfile = Profile.objects.get(user=getUser)
        if getProfile.usertype == 2:
            getStore = Store.objects.get(mystore=username)
            serializer = StoreSerializer(getStore)
            return Response(serializer.data)
        return Response(status=status.HTTP_400_BAD_REQUEST)

 # create a new order
    def post(self, request, username):
        getUser = User.objects.get(username=username)
        getProfile = Profile.objects.get(user=getUser)
        if getProfile.usertype == 2:
            try:
                getStore = Store.objects.get(mystore=username)
            except Store.DoesNotExist:
                Store.objects.create(mystore=username)
                getStore = Store.objects.get(mystore=username)
            request.data['store']=getStore.pk
            serializer = ProductSerializer(data=request.data)
            if serializer.is_valid():
                serializer.save()
                return Response(serializer.data, status=status.HTTP_201_CREATED)
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
        return Response(status=status.HTTP_400_BAD_REQUEST)

    def put(self, request, username):
        getUser = User.objects.get(username=username)
        getProfile = Profile.objects.get(user=getUser)
        if getProfile.usertype == 2:
            getStore = Store.objects.get(mystore=username)

            productName=request.data['myproduct']
            getproduct=Product.objects.get(store=getStore ,myproduct=productName)

            serializer = ProductSerializer(getproduct, data=request.data)
            if serializer.is_valid():
                serializer.save()
                return Response(serializer.data)
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
        return Response(status=status.HTTP_400_BAD_REQUEST)


class MyDroneViewSet(APIView):

    def get(self, request, username,ordername):
        getUser = User.objects.get(username=username)
        getOrder = Order.objects.get(user=getUser, orderName=ordername )
        getdrone = Drone.objects.get(order=getOrder)
        serializer = MyDroneSerializer(getdrone)
        return Response(serializer.data)


class ConfirmOrderViewSet(APIView):

    def put(self,request, username,ordername):
        try:
            getUser = User.objects.get(username=username)
            getOrder = Order.objects.get(user=getUser, orderName=ordername)
            getOrder.status = '3'
            getOrder.save()
            data = {
                'ORDER_STATUS': 'delivered'
            }
            return JsonResponse(data)
        except (Order.DoesNotExist or User.DoesNotExist):
            data = {
                'Error': 'Wrong format'
            }
            return JsonResponse(data)


stripe.api_key = 'sk_test_gPjkPryGzISTHGBiyypbdNyl'

class PaymentViewSet(APIView):

    def post(self, request, username,ordername):


        token = request.data['token']
        price = request.data['price']
        print(price)                                               ## payment = request.data['amount']# Using Flask
        source=token.get('mId')

        try:
            charge = stripe.Charge.create(
                amount=price,
                currency='cad',
                description='Example charge',
                source=source,
            )

            getUser = User.objects.get(username=username)
            getOrder = Order.objects.get(user=getUser, orderName=ordername)

            getOrder.status = '1'
            getOrder.save()

            data = {
                'Transaction': True
            }
            return JsonResponse(data)
            pass
        except Exception as e:
            data = {
                'Transaction': False
            }
            return JsonResponse(data)
        pass