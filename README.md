![patrius_logo](http://patrius.cnes.fr/resources/assets/wiki.png "Patrius logo") 


# PATRIUS Source Code

This project contains the source code of the PATRIUS-DATASET library. 
Although this code calls the latest version of PATRIUS,**Patrius v4.16**, it is also compatible with all previous versions of Patrius > v4.12.

## ABOUT

PATRIUS-DATASET is a small library developed by the [CNES](http://cnes.fr) and set of environment data for PATRIUS.

It makes quite easy to configure PATRIUS data for these different usages:

Solar activity for atmospheric models needs
Earth Orientation Parameters (EOP)
Third bodies ephemeris from JPL (DE 405 / 406)
Earth potential models, oceanic tides data
Geomagnetic models
Time scales (UTC-TAI)


Link this library and add this simple line of code at the beginning of your program and it will be fully operational!

PatriusDataset.addResourcesFromPatriusDataset();

PATRIUS_DATASET is regularly updated with up-to-date data, but PATRIUS users can still use low-level mechanisms to access their own specific data.


## PROJECT DOCUMENTATION

PATRIUS has its own Wiki accessible at the following address: http://patrius.cnes.fr
Through this link, you can also access the project overview, architecture and development, detailed features list, Javadoc and a lot more information.


## RELEASES

Official releases are available on https://www.connectbycnes.fr/en/patriusdataset.


## SUPPORT

patrius@cnes.fr


## LICENCE

PATRIUS is under Apache Licence 2.0.
