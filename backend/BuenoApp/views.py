from django.shortcuts import render

from django.shortcuts import get_object_or_404
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status, viewsets
from .models import Order, Drone, User
from .serializers import OrderSerializer, UserSerializer #, SignUp_Request_Serializer
from django.http import Http404

from rest_framework.generics import (
ListAPIView,
RetrieveAPIView,
UpdateAPIView,
DestroyAPIView,
RetrieveUpdateAPIView,
CreateAPIView,
)

from rest_framework.filters import (
SearchFilter,
OrderingFilter,
)


from rest_framework import generics

# See before tutorial 13 for permissions (most likely)
# from .permissions

# Create your views here.





# Class based view
# Lists all orders or creates a new one
# orders/
class OrderList(APIView):

    # return all the orders that we have
    def get(self, request):
        orders = Order.objects.all()
        # Convert now to JSON
        serializer = OrderSerializer(orders, many = True) # many = True specifies that there are many JSON objs to return

        filter_backends = [SearchFilter]
        search_fields = ['orderName', 'id']

        # Return HTTP Response
        return Response(serializer.data) #JSON Data

    # create a new order
    def post(self, request):
        serializer = OrderSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

class OrderViewSet(viewsets.ModelViewSet):

    queryset = Order.objects.all().order_by('-id')
    serializer_class = OrderSerializer
    filter_backends = [SearchFilter]
    search_fields = ['orderName','id']


class OrderDetail(APIView):
    def get_object(self, pk):
        try:
            return Order.objects.get(pk=pk)
        except Order.DoesNotExist:
            raise Http404

    def get(self, request, pk):
        snippet = self.get_object(pk)
        serializer = OrderSerializer(snippet)
        return Response(serializer.data)

    def put(self, request, pk):
        snippet = self.get_object(pk)
        serializer = OrderSerializer(snippet, data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    def delete(self, request, pk):
        snippet = self.get_object(pk)
        snippet.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)


from rest_framework.authentication import SessionAuthentication, BasicAuthentication
from rest_framework.permissions import IsAuthenticated
from rest_framework.response import Response
from rest_framework.views import APIView

# class ExampleView(APIView):
#     authentication_classes = (SessionAuthentication, BasicAuthentication)
#     permission_classes = (IsAuthenticated,)
#
#     def get(self, request, format=None):
#         content = {
#             'user': unicode(request.user),  # `django.contrib.auth.User` instance.
#             'auth': unicode(request.auth),  # None
#         }
#         return Response(content)


class UserListView(generics.ListCreateAPIView):
    queryset = User.objects.all()
    serializer_class = UserSerializer


# class Process_Signup_Request(APIView):
#     def get_object(self, pk):
#         try:
#             return User.objects.get(pk=pk)
#         except User.DoesNotExist:
#             raise Http404
#
#     def post(self, request):
#         serializer = SignUp_Request_Serializer(data=request.data)
#
#         if serializer.is_valid():
#             serializer.save()
#
#             return Response(serializer.data, status=status.HTTP_201_CREATED)
#         return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)