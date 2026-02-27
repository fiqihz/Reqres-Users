import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys
import groovy.json.JsonSlurper
import custom.TestDataHandling as handleTestData

// Get data from data test
String sheetName = 'Users'
String locatorExcel = 'Reqres.xlsx'
List<HashMap> listHashMapAccount = handleTestData.readTestData(locatorExcel, sheetName, true)
int rowCount = listHashMapAccount.size()

for (HashMap hash in listHashMapAccount[0..<rowCount]) {
	String page = hash.get('Page')
	int responseStatus = Integer.parseInt(hash.get('Response Status'))
	int dataSize = Integer.parseInt(hash.get('Data Size'))
	String totalData = hash.get('Total Data')
	String totalPages = hash.get('Total Pages')
	
	// Send Request (dynamic parameter)
	def response = WS.sendRequest(findTestObject('Object Repository/List Users', ['page' : page]))
	
	// Verify response status
	WS.verifyResponseStatusCode(response, responseStatus)
	
	// Verify page
	WS.verifyElementPropertyValue(response, 'page', page)
	
	// Verify total data per page
	def actualDataSize = WS.getElementPropertyValue(response, 'data.size()')
	assert actualDataSize == dataSize
	
	// Verify total data
	WS.verifyElementPropertyValue(response, 'total', totalData)
	
	// Verify total page
	WS.verifyElementPropertyValue(response, 'total_pages', totalPages)
	
	// validate all objects in array
	def allUsers = WS.getElementPropertyValue(response, 'data')
	allUsers.each { user ->
		assert user.id != null
		assert user.email != null
		assert user.first_name != null
		assert user.last_name != null
		assert user.avatar != null
	}
	
	// Validate support objects in array
	def support = WS.getElementPropertyValue(response, 'support')
	assert support.url != null
	assert support.text != null
}

