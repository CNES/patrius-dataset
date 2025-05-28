# PATRIUS-DATASET

This repository contains the source code for the **PATRIUS-DATASET** library. While it is designed to work seamlessly with the latest **Patrius v4.16**, it remains fully compatible with all previous versions from **Patrius v4.12 onward**.

## ABOUT

PATRIUS-DATASET is a lightweight library developed by [CNES](http://cnes.fr) that provides a comprehensive set of environmental data tailored for use with PATRIUS.

It simplifies the configuration of PATRIUS data for a variety of applications, including:

- Solar activity data for atmospheric modeling
- Earth Orientation Parameters (EOP)
- Ephemerides of third bodies from JPL (DE 405 / 406)
- Earth gravity potential models and oceanic tide data
- Geomagnetic models
- Time scales (UTC-TAI)

To get started, simply link this library and add the following line at the beginning of your program to enable full functionality:

`PatriusDataset.addResourcesFromPatriusDataset();`

The PATRIUS-DATASET library is regularly updated with the latest data. However, PATRIUS users also retain the flexibility to access custom data through lower-level mechanisms if needed.


## PROJECT DOCUMENTATION

PATRIUS offers extensive documentation, including a project overview, system architecture, feature lists, and Javadoc references. You can explore these resources on the official Wiki at: http://patrius.cnes.fr


## RELEASES

Official releases of PATRIUS-DATASET are available at: https://www.connectbycnes.fr/en/patriusdataset.


## SUPPORT

For questions or support, please contact: patrius@cnes.fr


## LICENCE

PATRIUS is under Apache Licence 2.0.
