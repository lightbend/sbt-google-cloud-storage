# Google Cloud Storage SBT plugin
An SBT dependency resolver and publisher for Google Cloud Storage. The plugin works out of one bucket at a time. This bucket has to be created and configured before using the plugin.

## Authentication with Google Cloud

### Provide download only dependencies 

If you want to provide dependencies for the public, the only thing you have to do is to make sure your bucket has public read access rights set. This is done automatically for you if you publish your project using this plugin, e.g. it sets the `AccessRights.PublicRead` flag for you.

### Publishing and resolving from private buckets

If you want to publish dependencies, you need to create a service token in the Google Cloud console.

- Create a json token file.
- The service token should be set to "Owner".
- Configure the following environment variable to point at the token json file\
 `export GOOGLE_APPLICATION_CREDENTIALS=<your-file-here>` 
- You may have to log out and then log in for the environment variable to be read again. 

## Usage

Add the following to `plugins.sbt` in your project:

`addSbtPlugin("com.lightbend" % "sbt-google-cloud-storage" % "0.0.10")`

### Using the plugin in your project

To use this plugin as a resolver, add the following to build.sbt.

`resolvers += GCSResolver.forBucket("bucket-name")`

To (optionally) use the plugin to publish artifacts.

`publishTo := Some(GCSPublisher.forBucket("bucket-name", AccessRights.InheritBucket))`

The publisher takes a second parameter that can be either of these values:

- `AccessRights.InheritBucket` - if you want the content to inherit the bucket access rights. 
- `AccessRights.PublicRead` - if you want the content to be publicly available for download. 

## Maintenance notes

This plugin is NOT supported under the Lightbend subscription.

The project is maintained by the contributors. Pull requests are very welcome. Thanks in advance!

## Limitations

- This project has only been tested using Scala 2.12.x and SBT 1.2.x.
- This plugin alway uses Ivy style publishing.
