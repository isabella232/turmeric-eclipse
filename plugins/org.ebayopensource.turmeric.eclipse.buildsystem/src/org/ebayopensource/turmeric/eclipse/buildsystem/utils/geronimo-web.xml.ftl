<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://geronimo.apache.org/xml/ns/j2ee/web-1.1" xmlns:nam="http://geronimo.apache.org/xml/ns/naming-1.1" xmlns:sec="http://geronimo.apache.org/xml/ns/security-1.1" xmlns:sys="http://geronimo.apache.org/xml/ns/deployment-1.1">
  <sys:environment>
    <sys:moduleId>
      <sys:groupId>eBay</sys:groupId>
      <sys:artifactId>${serviceName}</sys:artifactId>
      <sys:version>1.0.0</sys:version>
      <sys:type>war</sys:type>
    </sys:moduleId>
    <sys:dependencies>
      <sys:dependency>
        <sys:groupId>geronimo</sys:groupId>
        <sys:artifactId>sharedlib</sys:artifactId>
        <sys:type>car</sys:type>
      </sys:dependency>
    </sys:dependencies>
  </sys:environment>
  <context-root>/${serviceName}</context-root>
</web-app>