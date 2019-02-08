# Using Application

Make the following steps:
- Register an account or login to the Mobile Connect Developer Portal and create an application to get your sandbox credentials.
- Download the client side library.
- Configure your project. Please reference the [demo application](../).
- Build and run the application.

Application has two modes:
- With Discovery
- Without Discovery


# With Discovery

With your configuration specified in the configuration file or using user interface, you have everything you need to make a successful call to the server side library.
You can use the client side with discovery application in three modes:

- MSISDN mode
The request to your server side part in the MSISDN mode: ```http://server_side_endpoint?msisdn=your_msisdn```.
The default MSISDN value is from the configuration.xml file.
- MCC_MNC mode
The request to your server side part in the MCC_MNC mode: ```http://server_side_endpoint?mcc=your_mcc&mnc=your_mnc```.
The default MCC and MNC values are extracted from the current device SIM card.
- None mode
The request to your server side part in the NONE mode: ```http://server_side_endpoint?```.

Including IP Address.
The IP Address can be added in each mode.
The request to your server side SDK in the MSISDN mode including the IP Address: ```http://server_side_endpoint?msisdn=your_msisdn&ip_address=your_ip_address```.

# Without Discovery
With your configuration specified in the configuration (```server_endpoint_without_discovery_endpoint``` and ```msisdn_wd params```) file or using user interface, you have everything you need to make a successful call to the server side library into the without discovery mode.
Without Discovery works in MSISDN mode only.
THe example request: ```http://server_side_without_discovery_endpoint?msisdn=your_msisdn```

#General
After pressing Mobile Connect button you will see Web View Dialog.
Here you can find session status.
Please check 'References' item to navigate details like source code.

#References
- [Android library](https://developer.mobileconnect.io/android-library)
