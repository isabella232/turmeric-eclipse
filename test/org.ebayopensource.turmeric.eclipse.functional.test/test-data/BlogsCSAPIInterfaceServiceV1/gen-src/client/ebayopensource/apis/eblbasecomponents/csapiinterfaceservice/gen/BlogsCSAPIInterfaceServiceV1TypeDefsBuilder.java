
package ebayopensource.apis.eblbasecomponents.csapiinterfaceservice.gen;

import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.namespace.QName;
import org.ebayopensource.turmeric.runtime.common.impl.internal.schema.BaseTypeDefsBuilder;
import org.ebayopensource.turmeric.runtime.common.impl.internal.schema.FlatSchemaComplexTypeImpl;
import org.ebayopensource.turmeric.runtime.common.impl.internal.schema.FlatSchemaElementDeclImpl;
import org.ebayopensource.turmeric.runtime.common.types.SOAFrameworkCommonTypeDefsBuilder;


/**
 * Note : Generated file, any changes will be lost upon regeneration.
 * 
 */
public class BlogsCSAPIInterfaceServiceV1TypeDefsBuilder
    extends BaseTypeDefsBuilder
{

    private final static String NS1 = "urn:ebayopensource:apis:eBLBaseComponents";

    public void build() {
        ArrayList<FlatSchemaComplexTypeImpl> complexTypes = new ArrayList<FlatSchemaComplexTypeImpl>();
        addComplexTypes0(complexTypes);
         
        addComplexTypeElements0(complexTypes);
         
        HashMap<QName, FlatSchemaElementDeclImpl> rootElements = new HashMap<QName, FlatSchemaElementDeclImpl>();
        addRootElements0(complexTypes, rootElements);
         
        SOAFrameworkCommonTypeDefsBuilder.includeTypeDefs(complexTypes, rootElements);
         
        m_complexTypes = complexTypes;
        m_rootElements = rootElements;
    }

    private void addComplexTypes0(ArrayList<FlatSchemaComplexTypeImpl> complexTypes) {
        // Type #0 (BotBlockResponseType)
        complexTypes.add(new FlatSchemaComplexTypeImpl(new QName(NS1, "BotBlockResponseType")));
        // Type #1 (DuplicateInvocationDetailsType)
        complexTypes.add(new FlatSchemaComplexTypeImpl(new QName(NS1, "DuplicateInvocationDetailsType")));
        // Type #2 (ErrorType)
        complexTypes.add(new FlatSchemaComplexTypeImpl(new QName(NS1, "ErrorType")));
        // Type #3 (BotBlockRequestType)
        complexTypes.add(new FlatSchemaComplexTypeImpl(new QName(NS1, "BotBlockRequestType")));
        // Type #4 (MACAttachementFileType)
        complexTypes.add(new FlatSchemaComplexTypeImpl(new QName(NS1, "MACAttachementFileType")));
        // Type #5 (MachineTagTypeArrayType)
        complexTypes.add(new FlatSchemaComplexTypeImpl(new QName(NS1, "MachineTagTypeArrayType")));
        // Type #6 (AbstractRequestType)
        complexTypes.add(new FlatSchemaComplexTypeImpl(new QName(NS1, "AbstractRequestType")));
        // Type #7 (AbstractResponseType)
        complexTypes.add(new FlatSchemaComplexTypeImpl(new QName(NS1, "AbstractResponseType")));
        // Type #8 (CSUpdateMACActivityAddAttachmentsResponseType)
        complexTypes.add(new FlatSchemaComplexTypeImpl(new QName(NS1, "CSUpdateMACActivityAddAttachmentsResponseType")));
        // Type #9 (CSUserIdType)
        complexTypes.add(new FlatSchemaComplexTypeImpl(new QName(NS1, "CSUserIdType")));
        // Type #10 (XMLRequesterCredentialsType)
        complexTypes.add(new FlatSchemaComplexTypeImpl(new QName(NS1, "XMLRequesterCredentialsType")));
        // Type #11 (CSUpdateMACActivityAddAttachmentsRequestType)
        complexTypes.add(new FlatSchemaComplexTypeImpl(new QName(NS1, "CSUpdateMACActivityAddAttachmentsRequestType")));
        // Type #12 (CustomSecurityHeaderType)
        complexTypes.add(new FlatSchemaComplexTypeImpl(new QName(NS1, "CustomSecurityHeaderType")));
        // Type #13 (ErrorParameterType)
        complexTypes.add(new FlatSchemaComplexTypeImpl(new QName(NS1, "ErrorParameterType")));
        // Type #14 (UserIdPasswordType)
        complexTypes.add(new FlatSchemaComplexTypeImpl(new QName(NS1, "UserIdPasswordType")));
    }

    private void addComplexTypeElements0(ArrayList<FlatSchemaComplexTypeImpl> complexTypes) {
        FlatSchemaComplexTypeImpl currType;
         
        // Type #0 (BotBlockResponseType)
        currType = complexTypes.get(0);
        currType.addSimpleElement(new QName(NS1, "BotBlockToken"), 1);
        currType.addSimpleElement(new QName(NS1, "BotBlockUrl"), 1);
        currType.addSimpleElement(new QName(NS1, "BotBlockAudioUrl"), 1);
         
        // Type #1 (DuplicateInvocationDetailsType)
        currType = complexTypes.get(1);
        currType.addSimpleElement(new QName(NS1, "DuplicateInvocationID"), 1);
        currType.addSimpleElement(new QName(NS1, "Status"), 1);
        currType.addSimpleElement(new QName(NS1, "InvocationTrackingID"), 1);
         
        // Type #2 (ErrorType)
        currType = complexTypes.get(2);
        currType.addSimpleElement(new QName(NS1, "ShortMessage"), 1);
        currType.addSimpleElement(new QName(NS1, "LongMessage"), 1);
        currType.addSimpleElement(new QName(NS1, "ErrorCode"), 1);
        currType.addSimpleElement(new QName(NS1, "UserDisplayHint"), 1);
        currType.addSimpleElement(new QName(NS1, "SeverityCode"), 1);
        currType.addComplexElement(new QName(NS1, "ErrorParameters"), complexTypes.get(13), -1);
        currType.addSimpleElement(new QName(NS1, "ErrorClassification"), 1);
         
        // Type #3 (BotBlockRequestType)
        currType = complexTypes.get(3);
        currType.addSimpleElement(new QName(NS1, "BotBlockToken"), 1);
        currType.addSimpleElement(new QName(NS1, "BotBlockUserInput"), 1);
         
        // Type #4 (MACAttachementFileType)
        currType = complexTypes.get(4);
        currType.addSimpleElement(new QName(NS1, "Name"), 1);
        currType.addSimpleElement(new QName(NS1, "Path"), 1);
        currType.addSimpleElement(new QName(NS1, "Size"), 1);
        currType.addSimpleElement(new QName(NS1, "Data"), 1);
         
        // Type #5 (MachineTagTypeArrayType)
        currType = complexTypes.get(5);
        currType.addSimpleElement(new QName(NS1, "TagType"), -1);
         
        // Type #6 (AbstractRequestType)
        currType = complexTypes.get(6);
        currType.addSimpleElement(new QName(NS1, "DetailLevel"), -1);
        currType.addSimpleElement(new QName(NS1, "ErrorLanguage"), 1);
        currType.addSimpleElement(new QName(NS1, "MessageID"), 1);
        currType.addSimpleElement(new QName(NS1, "Version"), 1);
        currType.addSimpleElement(new QName(NS1, "EndUserIP"), 1);
        currType.addSimpleElement(new QName(NS1, "PostTransactionData"), 1);
        currType.addSimpleElement(new QName(NS1, "UsageData"), 1);
        currType.addSimpleElement(new QName(NS1, "BulkJobID"), 1);
        currType.addSimpleElement(new QName(NS1, "BulkTaskID"), 1);
        currType.addSimpleElement(new QName(NS1, "GUID"), 1);
        currType.addSimpleElement(new QName(NS1, "HTTPUserAgent"), 1);
        currType.addSimpleElement(new QName(NS1, "HTTPAccept"), 1);
        currType.addSimpleElement(new QName(NS1, "HTTPAcceptLanguage"), 1);
        currType.addSimpleElement(new QName(NS1, "HTTPAcceptCharset"), 1);
        currType.addSimpleElement(new QName(NS1, "HTTPAcceptEncoding"), 1);
        currType.addSimpleElement(new QName(NS1, "Priority"), 1);
        currType.addComplexElement(new QName(NS1, "RequesterCredentials"), complexTypes.get(10), 1);
        currType.addSimpleElement(new QName(NS1, "ErrorHandling"), 1);
        currType.addSimpleElement(new QName(NS1, "InvocationID"), 1);
        currType.addSimpleElement(new QName(NS1, "OutputSelector"), -1);
        currType.addSimpleElement(new QName(NS1, "WarningLevel"), 1);
        currType.addComplexElement(new QName(NS1, "BotBlock"), complexTypes.get(3), 1);
        currType.addSimpleElement(new QName(NS1, "MachineGroupID"), 1);
        currType.addSimpleElement(new QName(NS1, "MachineGroupValue"), 1);
        currType.addComplexElement(new QName(NS1, "MachineTagTypes"), complexTypes.get(5), 1);
         
        // Type #7 (AbstractResponseType)
        currType = complexTypes.get(7);
        currType.addSimpleElement(new QName(NS1, "Timestamp"), 1);
        currType.addSimpleElement(new QName(NS1, "Ack"), 1);
        currType.addSimpleElement(new QName(NS1, "CorrelationID"), 1);
        currType.addComplexElement(new QName(NS1, "Errors"), complexTypes.get(2), -1);
        currType.addSimpleElement(new QName(NS1, "Message"), 1);
        currType.addSimpleElement(new QName(NS1, "Version"), 1);
        currType.addSimpleElement(new QName(NS1, "Build"), 1);
        currType.addSimpleElement(new QName(NS1, "PostTransactionData"), 1);
        currType.addSimpleElement(new QName(NS1, "UsageData"), 1);
        currType.addSimpleElement(new QName(NS1, "GUID"), 1);
        currType.addSimpleElement(new QName(NS1, "FilteredElement"), -1);
        currType.addSimpleElement(new QName(NS1, "JobID"), 1);
        currType.addSimpleElement(new QName(NS1, "Complexity"), 1);
        currType.addSimpleElement(new QName(NS1, "NotificationEventName"), 1);
        currType.addComplexElement(new QName(NS1, "DuplicateInvocationDetails"), complexTypes.get(1), 1);
        currType.addSimpleElement(new QName(NS1, "RecipientUserID"), 1);
        currType.addSimpleElement(new QName(NS1, "EIASToken"), 1);
        currType.addSimpleElement(new QName(NS1, "NotificationSignature"), 1);
        currType.addSimpleElement(new QName(NS1, "HardExpirationWarning"), 1);
        currType.addComplexElement(new QName(NS1, "BotBlock"), complexTypes.get(0), 1);
         
        // Type #8 (CSUpdateMACActivityAddAttachmentsResponseType)
        currType = complexTypes.get(8);
        currType.addSimpleElement(new QName(NS1, "Timestamp"), 1);
        currType.addSimpleElement(new QName(NS1, "Ack"), 1);
        currType.addSimpleElement(new QName(NS1, "CorrelationID"), 1);
        currType.addComplexElement(new QName(NS1, "Errors"), complexTypes.get(2), -1);
        currType.addSimpleElement(new QName(NS1, "Message"), 1);
        currType.addSimpleElement(new QName(NS1, "Version"), 1);
        currType.addSimpleElement(new QName(NS1, "Build"), 1);
        currType.addSimpleElement(new QName(NS1, "PostTransactionData"), 1);
        currType.addSimpleElement(new QName(NS1, "UsageData"), 1);
        currType.addSimpleElement(new QName(NS1, "GUID"), 1);
        currType.addSimpleElement(new QName(NS1, "FilteredElement"), -1);
        currType.addSimpleElement(new QName(NS1, "JobID"), 1);
        currType.addSimpleElement(new QName(NS1, "Complexity"), 1);
        currType.addSimpleElement(new QName(NS1, "NotificationEventName"), 1);
        currType.addComplexElement(new QName(NS1, "DuplicateInvocationDetails"), complexTypes.get(1), 1);
        currType.addSimpleElement(new QName(NS1, "RecipientUserID"), 1);
        currType.addSimpleElement(new QName(NS1, "EIASToken"), 1);
        currType.addSimpleElement(new QName(NS1, "NotificationSignature"), 1);
        currType.addSimpleElement(new QName(NS1, "HardExpirationWarning"), 1);
        currType.addComplexElement(new QName(NS1, "BotBlock"), complexTypes.get(0), 1);
         
        // Type #9 (CSUserIdType)
        currType = complexTypes.get(9);
        currType.addSimpleElement(new QName(NS1, "UserID"), 1);
        currType.addSimpleElement(new QName(NS1, "EmailAddress"), 1);
        currType.addSimpleElement(new QName(NS1, "UserOracleID"), 1);
         
        // Type #10 (XMLRequesterCredentialsType)
        currType = complexTypes.get(10);
        currType.addSimpleElement(new QName(NS1, "Username"), 1);
        currType.addSimpleElement(new QName(NS1, "Password"), 1);
        currType.addSimpleElement(new QName(NS1, "eBayAuthToken"), 1);
         
        // Type #11 (CSUpdateMACActivityAddAttachmentsRequestType)
        currType = complexTypes.get(11);
        currType.addSimpleElement(new QName(NS1, "DetailLevel"), -1);
        currType.addSimpleElement(new QName(NS1, "ErrorLanguage"), 1);
        currType.addSimpleElement(new QName(NS1, "MessageID"), 1);
        currType.addSimpleElement(new QName(NS1, "Version"), 1);
        currType.addSimpleElement(new QName(NS1, "EndUserIP"), 1);
        currType.addSimpleElement(new QName(NS1, "PostTransactionData"), 1);
        currType.addSimpleElement(new QName(NS1, "UsageData"), 1);
        currType.addSimpleElement(new QName(NS1, "BulkJobID"), 1);
        currType.addSimpleElement(new QName(NS1, "BulkTaskID"), 1);
        currType.addSimpleElement(new QName(NS1, "GUID"), 1);
        currType.addSimpleElement(new QName(NS1, "HTTPUserAgent"), 1);
        currType.addSimpleElement(new QName(NS1, "HTTPAccept"), 1);
        currType.addSimpleElement(new QName(NS1, "HTTPAcceptLanguage"), 1);
        currType.addSimpleElement(new QName(NS1, "HTTPAcceptCharset"), 1);
        currType.addSimpleElement(new QName(NS1, "HTTPAcceptEncoding"), 1);
        currType.addSimpleElement(new QName(NS1, "Priority"), 1);
        currType.addComplexElement(new QName(NS1, "RequesterCredentials"), complexTypes.get(10), 1);
        currType.addSimpleElement(new QName(NS1, "ErrorHandling"), 1);
        currType.addSimpleElement(new QName(NS1, "InvocationID"), 1);
        currType.addSimpleElement(new QName(NS1, "OutputSelector"), -1);
        currType.addSimpleElement(new QName(NS1, "WarningLevel"), 1);
        currType.addComplexElement(new QName(NS1, "BotBlock"), complexTypes.get(3), 1);
        currType.addSimpleElement(new QName(NS1, "MachineGroupID"), 1);
        currType.addSimpleElement(new QName(NS1, "MachineGroupValue"), 1);
        currType.addComplexElement(new QName(NS1, "MachineTagTypes"), complexTypes.get(5), 1);
        currType.addComplexElement(new QName(NS1, "User"), complexTypes.get(9), 1);
        currType.addSimpleElement(new QName(NS1, "ActivityID"), 1);
        currType.addComplexElement(new QName(NS1, "Attachment"), complexTypes.get(4), 1);
         
        // Type #12 (CustomSecurityHeaderType)
        currType = complexTypes.get(12);
        currType.addSimpleElement(new QName(NS1, "eBayAuthToken"), 1);
        currType.addSimpleElement(new QName(NS1, "HardExpirationWarning"), 1);
        currType.addComplexElement(new QName(NS1, "Credentials"), complexTypes.get(14), 1);
        currType.addSimpleElement(new QName(NS1, "NotificationSignature"), 1);
         
        // Type #13 (ErrorParameterType)
        currType = complexTypes.get(13);
        currType.addAttribute(new QName(NS1, "ParamID"));
        currType.addSimpleElement(new QName(NS1, "Value"), 1);
         
        // Type #14 (UserIdPasswordType)
        currType = complexTypes.get(14);
        currType.addSimpleElement(new QName(NS1, "AppId"), 1);
        currType.addSimpleElement(new QName(NS1, "DevId"), 1);
        currType.addSimpleElement(new QName(NS1, "AuthCert"), 1);
        currType.addSimpleElement(new QName(NS1, "Username"), 1);
        currType.addSimpleElement(new QName(NS1, "Password"), 1);
    }

    private void addRootElements0(ArrayList<FlatSchemaComplexTypeImpl> complexTypes, HashMap<QName, FlatSchemaElementDeclImpl> rootElements) {
        rootElements.put(new QName(NS1, "CSUpdateMACActivityAddAttachmentsRequest"), FlatSchemaElementDeclImpl.createRootComplexElement(new QName(NS1, "CSUpdateMACActivityAddAttachmentsRequest"), complexTypes.get(11)));
        rootElements.put(new QName(NS1, "CSUpdateMACActivityAddAttachmentsResponse"), FlatSchemaElementDeclImpl.createRootComplexElement(new QName(NS1, "CSUpdateMACActivityAddAttachmentsResponse"), complexTypes.get(8)));
        rootElements.put(new QName(NS1, "RequesterCredentials"), FlatSchemaElementDeclImpl.createRootComplexElement(new QName(NS1, "RequesterCredentials"), complexTypes.get(12)));
    }

}
