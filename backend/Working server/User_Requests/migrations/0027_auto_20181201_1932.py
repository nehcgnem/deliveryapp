# Generated by Django 2.1.1 on 2018-12-02 00:32

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('User_Requests', '0026_auto_20181201_1901'),
    ]

    operations = [
        migrations.AlterField(
            model_name='profile',
            name='userCoord',
            field=models.CharField(default='1,1', max_length=100),
        ),
    ]
