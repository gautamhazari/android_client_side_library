GSMA MobileConnect Android Client Side Library
==============================================================================================================
Mobile Connect is a mobile identity service based on the OpenID Connect & OAuth2 where end users can authenticate themselves using their mobile phone via Mobile Connect. This allows them access to websites and applications without the need to remember passwords and usernames. It’s safe, secure and no personal information is shared without their permission.

This example client side application demonstrates how to integrate client side library with server side library.

Note: if you operate in the EU then you should use EU Discovery Service domain in discovery URL: eu.discover.mobileconnect.io

## Pre-requisites
- Android SDK 20 or higher
- Android Build Tools v27.0.3 or higher
- Gradle Wrapper v3.5.1 or higher

## Quick Start
- Download the Mobile Connect client side library.
- Open project in Android Studio.
- Download and install any missing dependencies or plugins from the links displayed in the console output.
- Build the project.
- Open the configuration file: (local-path/Application/src/main/res/values/configuration.xml/).
Here are the following parameters for With Discovery and Without Discovery modes:
(<string name="server_endpoint_with_discovery_endpoint">your server side endpoint for demo with discovery</string>
    <string name="server_endpoint_without_discovery_endpoint">your server side endpoint for demo without discovery</string>
    <string name="msisdn">default msisdn</string>
    <string name="msisdn_wd">default msisdn for without discovery mode</string>
    <string name="mcc_value">default mcc</string>
    <string name="mnc_value">default mnc</string>)
Be sure to provide your server side endpoint or server side without discovery endpoint to interact with server side SDK.

Note: you can not change the server side endpoints while application is running. 

You can also configure your MSISDN, MCC and MNC, IP address for With Discovery App if it's necessary. Or you can configure MSISDN for With Discovery App. You can change the parameters above while application is running (expect server side endpoints).

- Build the [Application](./Application/) and [ClientSideLibrary](./ClientSideLibrary/).
- The Application works in three modes (MSISDN, MCC_MNC, None). Also you can use the IP Address as optional item for each mode.
- With your configuration specified in the configuration file and using user interface, you have everything you need to make a successful call to the server side library by pressing 'Mobile Connect' button.

Note: Your client side library will work only with server side library. Please, see links to the [server side libraries](#resources).

## Support

If you encounter any issues which are not resolved by consulting the resources below then [send us a message](https://developer.mobileconnect.io/content/contact-us)

## Resources
- [MobileConnect Android Client Side Library](https://developer.mobileconnect.io/content/android-client-side-library)
- [MobileConnect Java Server Side Library](https://developer.mobileconnect.io/content/java-server-side-library)
- [MobileConnect .NET Server Side Library](https://developer.mobileconnect.io/content/net-server-side-library)
- [MobileConnect PHP Server Side Library](https://developer.mobileconnect.io/content/php-server-side-library)
- [MobileConnect Authentication API Information](https://developer.mobileconnect.io/mobile-connect-api)
- [MobileConnect Authentication API (v2.0) Information](https://developer.mobileconnect.io/mobile-connect-profile-v2-0)
- [MobileConnect Discovery API Information](https://developer.mobileconnect.io/discovery-api)
