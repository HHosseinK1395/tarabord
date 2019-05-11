package Interfaces;

import Utilities.AddressNameAndDriversLocations;

public interface InterfaceAsyncTasks
{
    void ShowAddressTask_Callback(String output);
    void SyncShowAddress_Callback(AddressNameAndDriversLocations output);
}
