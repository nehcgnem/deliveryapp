# Generated by Django 2.1.1 on 2018-11-24 20:31

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('User_Requests', '0018_order_totalprice'),
    ]

    operations = [
        migrations.AddField(
            model_name='profile',
            name='usertype',
            field=models.PositiveIntegerField(blank=True, default=1),
        ),
    ]
