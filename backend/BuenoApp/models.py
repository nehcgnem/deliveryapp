from django.db import models
import uuid

# Create your models here.
from django.db import models
from django.contrib.auth.models import AbstractUser
from django.core.validators import RegexValidator
class User(AbstractUser):
    # name = models.CharField(max_length=100, blank=True, null=True)

    # othername = models.CharField(max_length=100, blank=True, null=True)



    # phone_regex = RegexValidator(regex=r'^\+?1?\d{9,15}$',
    #                              message="Phone number must be entered in the format: '+999999999'. Up to 15 digits allowed.")
    # phone_number = models.CharField(validators=[phone_regex], max_length=17, blank=True)  # validators should be a list

    pass


class Order(models.Model):
    """Model representing a book genre."""
    # id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False, help_text="Universally unique UUID")
    orderName = models.CharField(max_length=200, help_text="Enter an orderName (e.g. Bob's order)")

    def __str__(self):
        """String for representing the Model object."""
        return self.orderName

class Drone(models.Model):
    """Model representing a book genre."""
    # id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False, help_text="Universally unique UUID")
    droneName = models.CharField(max_length=200, help_text="Enter a drone name (e.g. Drone X)")

    def __str__(self):
        """String for representing the Model object."""
        return self.droneName