# Generated by Django 2.1.1 on 2018-11-20 20:34

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('User_Requests', '0003_remove_product_stores'),
    ]

    operations = [
        migrations.AddField(
            model_name='product',
            name='stores',
            field=models.ForeignKey(null=True, on_delete=django.db.models.deletion.CASCADE, to='User_Requests.Store'),
        ),
    ]
