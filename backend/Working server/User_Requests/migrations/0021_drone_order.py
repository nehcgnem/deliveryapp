# Generated by Django 2.1.1 on 2018-11-24 22:36

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('User_Requests', '0020_auto_20181124_1651'),
    ]

    operations = [
        migrations.AddField(
            model_name='drone',
            name='order',
            field=models.ForeignKey(null=True, on_delete=django.db.models.deletion.CASCADE, to='User_Requests.Order'),
        ),
    ]