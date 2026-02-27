package custom

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.testdata.reader.ExcelFactory

import internal.GlobalVariable

public class TestDataHandling {
	static String pathDataFile = RunConfiguration.getProjectDir() + "/DataSet/"
	
		static def List<HashMap> readTestData(String pathFile, String sheetName, boolean isUsingFirstRowAsHeader) throws IOException {
			int i,j
			String pathData = RunConfiguration.getProjectDir() + "/DataSet/"
			String pathFileTestData = pathData + pathFile
			List<HashMap> listHashMap = new ArrayList<HashMap>()
	
			try {
				TestData data = ExcelFactory.getExcelDataWithDefaultSheet(pathFileTestData, sheetName, isUsingFirstRowAsHeader)
	
				List<List<Object>> listAllData = data.getAllData()
				KeywordUtil.logInfo("Size data : " + listAllData.size())
	
				String[] getHeaderColumnName = data.getColumnNames()
	
				for (i = 0; i < listAllData.size(); i++) {
					List<Object> getListData = listAllData.get(i)
					HashMap<Object, Object> hashMapSetKeyAndValueFromTestData = new HashMap<Object, Object>()
	
					for (j = 0; j < getHeaderColumnName.length; j++) {
						String keyName = getHeaderColumnName[j]
						String valueData = getListData.get(j)
	
						if (!keyName.equals(null) || !keyName.equals("")) {
							if (valueData.equals(null) && !keyName.equals(null)) {
								hashMapSetKeyAndValueFromTestData.put(keyName, "")
							} else {
								hashMapSetKeyAndValueFromTestData.put(keyName, valueData)
							}
						}
					}
					listHashMap.add(hashMapSetKeyAndValueFromTestData)
				}
			} catch (Exception e) {
				KeywordUtil.markFailedAndStop(e.getMessage())
			}
			return listHashMap
		}
}
