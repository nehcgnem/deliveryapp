from django.db import models
from django.db import models
from django.contrib.auth.models import User
from django.db.models.signals import post_save,pre_save
from django.dispatch import receiver
# Create your models here.
from django.db import models
from django.contrib.auth.models import User
from decimal import *

class Profile(models.Model):
    user = models.OneToOneField(User, on_delete=models.CASCADE)
    bio = models.TextField(max_length=500, blank=True)
    location = models.CharField(max_length=30, blank=True)
    birth_date = models.DateField(null=True, blank=True)
    ##order = models.ForeignKey('Order', on_delete=models.CASCADE,null=True)
    ##store = models.ManyToManyField('Store', blank=True)
    usertype = models.PositiveIntegerField(blank = True, default=1)
    userCoord = models.CharField(max_length=100, default='1,1')
@receiver(post_save, sender=User)
def create_user_profile(sender, instance, created, **kwargs):
    if created:
        Profile.objects.create(user=instance)

@receiver(post_save, sender=User)
def save_user_profile(sender, instance, **kwargs):
    instance.profile.save()



class Store(models.Model):## Everyone can access
    mystore = models.CharField(max_length=30, blank=True)

    storeProduct = models.ManyToManyField('Product',related_name="ProductStore", blank=True)


    def __str__(self):
        """String for representing the Model object."""
        return self.mystore

class Product(models.Model):## Everyone can access
    store = models.ForeignKey(Store,  unique=False, on_delete=models.CASCADE, null=True)
    myproduct = models.CharField(max_length=30, blank=True)
    price = models.PositiveIntegerField( default="0" , blank = True)
    description = models.TextField(max_length=300, blank=True)
    quantity = models.PositiveIntegerField(blank = True, default=0)

    # slug = models.SlugField(max_length=100, null=True)

    def __str__(self):
        """String for representing the Model object."""
        return self.myproduct

@receiver(post_save, sender=Product)
def create_product(sender, instance, created, **kwargs):
    if created:
        getStore = Store.objects.get(pk=instance.store.pk)
        getStore.storeProduct.add(instance)




class Order(models.Model):
    """Model representing a book genre."""
    user = models.ForeignKey(User, unique=False,on_delete=models.CASCADE,null=True)
    # id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False, help_text="Universally unique UUID")
    orderName = models.CharField(max_length=200, help_text="Enter an orderName (e.g. Bob's order)")

    orderedProduct1 = models.CharField(max_length=30, blank=True)
    orderQuantity1 = models.PositiveIntegerField(blank=True, default=0)
    orderedProduct2 = models.CharField(max_length=30, blank=True)
    orderQuantity2 = models.PositiveIntegerField(blank=True, default=0)
    orderedProduct3 = models.CharField(max_length=30, blank=True)
    orderQuantity3 = models.PositiveIntegerField(blank=True, default=0)
    orderedProduct4 = models.CharField(max_length=30, blank=True)
    orderQuantity4 = models.PositiveIntegerField(blank=True, default=0)
    orderedProduct5 = models.CharField(max_length=30, blank=True)
    orderQuantity5 = models.PositiveIntegerField(blank=True, default=0)
    orderedProduct6 = models.CharField(max_length=30, blank=True)
    orderQuantity6 = models.PositiveIntegerField(blank=True, default=0)
    orderedProduct7 = models.CharField(max_length=30, blank=True)
    orderQuantity7 = models.PositiveIntegerField(blank=True, default=0)
    orderedProduct8 = models.CharField(max_length=30, blank=True)
    orderQuantity8 = models.PositiveIntegerField(blank=True, default=0)
    orderedProduct9 = models.CharField(max_length=30, blank=True)
    orderQuantity9 = models.PositiveIntegerField(blank=True, default=0)
    orderedProduct10 = models.CharField(max_length=30, blank=True)
    orderQuantity10 = models.PositiveIntegerField(blank=True, default=0)

    ORDER_STATUS = (
        ('0', 'processing'),
        ('1', 'outforpickup'),
        ('2', 'outfordelivery'),
        ('3', 'delivered'),
    )

    status = models.CharField(max_length=1, choices=ORDER_STATUS, default='0')

    totalPrice = models.DecimalField(max_digits=6, decimal_places=2, default="0.00" , blank = True)

    def __str__(self):
        """String for representing the Model object."""
        return self.orderName

@receiver(pre_save, sender=Order)
def create_order(sender, instance, **kwargs):
    itemsList = list()

    productList = list()
    productList.append(instance.orderedProduct1)
    productList.append(instance.orderedProduct2)
    productList.append(instance.orderedProduct3)
    productList.append(instance.orderedProduct1)
    productList.append(instance.orderedProduct4)
    productList.append(instance.orderedProduct5)
    productList.append(instance.orderedProduct6)
    productList.append(instance.orderedProduct7)
    productList.append(instance.orderedProduct8)
    productList.append(instance.orderedProduct9)
    productList.append(instance.orderedProduct10)

    quantityList = list()
    quantityList.append(instance.orderQuantity1)
    quantityList.append(instance.orderQuantity2)
    quantityList.append(instance.orderQuantity3)
    quantityList.append(instance.orderQuantity1)
    quantityList.append(instance.orderQuantity4)
    quantityList.append(instance.orderQuantity5)
    quantityList.append(instance.orderQuantity6)
    quantityList.append(instance.orderQuantity7)
    quantityList.append(instance.orderQuantity8)
    quantityList.append(instance.orderQuantity9)
    quantityList.append(instance.orderQuantity10)


    itemsList.append(productList)
    itemsList.append(quantityList)
    totalCost = 0.00

    for i in range(1,11):
        # if(instance.orderedProduct1!=""):
        if(itemsList[0][i]!=""): # first index is simply the List (only 2)

            getProduct = Product.objects.get(myproduct=itemsList[0][i])#instance.orderedProduct1)
            newQuantity = getProduct.quantity - itemsList[1][i]#instance.orderQuantity1
            totalCost += float(getProduct.price)*itemsList[1][i]

            if(newQuantity<0):
                raise Exception('Product quantity not available')
            instance.totalPrice = Decimal(totalCost)
            getProduct.quantity = newQuantity
            getProduct.save()






class Drone(models.Model):
    """Model representing a book genre."""
    # id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False, help_text="Universally unique UUID")
    droneName = models.CharField(max_length=200, help_text="Enter a drone name (e.g. Drone X)")
    order = models.ForeignKey(Order, unique=False, on_delete=models.CASCADE, null=True, blank = True)
    storeCoord = models.CharField(max_length=100,  default='5,5')
    destinationCoord = models.CharField(max_length=100,  default='0,0')
    currentCoord = models.CharField(max_length=100,  default='0,0')
    def __str__(self):
        """String for representing the Model object."""
        return self.droneName

# class UserOrders(models.Model):
#     user = models.OneToOneField(User, on_delete=models.CASCADE)
#     order = models.ForeignKey('Order', on_delete=models.CASCADE)
# def __str__(self):
#     """String for representing the Model object."""
#     return str(self.order)
#
# @receiver(post_save, sender=Order)
# def create_user_orders(sender, instance, created, **kwargs):
#     if created:
#         UserOrders.objects.create(user=instance)
#
# @receiver(post_save, sender=Order)
# def save_user_orders(sender, instance, **kwargs):
#     instance.userorders.save()