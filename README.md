# Camdozaal Vault Helper

Upon vault entry, highlights all barriers and lockboxes in optimal route 
through the vault, with the next object to be interacted with highlighted 
a different color. Default route targets maximum ornate lockboxes but can be
toggled to target maximum elaborate lockboxes in plugin settings.

![Illustration](../assets/CamdozaalVaultHelperPlugin.png)

## Features

1. Toggle to prioritize elaborate lockboxes or ornate lockboxes. Certain layouts make
taking ornate or elaborate lockboxes impossible, meaning route can sometimes have 0 of target type.

2. Selectable route highlight colors for full path and next object.

3. Toggleable TextBox displaying estimated time in seconds route will take to complete,
the "Risk If Delayed," and the number of lockboxes in route. This risk means how likely 
a player is to fail to exit in time if they mess up. <ins>**Some of the routes are tick perfect;
if a mistake occurs when risk is HIGH (58.2-59.4s routes), consider skipping 
a chest if still possible.**</ins> I spam click the next object until character begins moving to
save time. (Note: skipping will break the next object highlighting
for the current run)

4. Toggleable InfoBox displaying amount of barronite shards stored in forge.

![Shards](../assets/BarroniteInfoBox.png)
