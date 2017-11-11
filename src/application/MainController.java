package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.ValueAxis;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import util.SettingHelper;
import util.StageHelper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.CheckBox;

public class MainController implements Initializable {
	@FXML
	private Label labelLoad;
	@FXML
	private TextField textFildLoad;
	@FXML
	private Button btnLoad;
	@FXML
	private Label labelSave;
	@FXML
	private TextField textFildSave;
	@FXML
	private Button btnSavePath;
	@FXML
	private Label labelId;
	@FXML
	private TextField textFildId;
	@FXML
	private Label labelDescriptio;
	@FXML
	private TextField textFiledDescription;
	@FXML
	private Label labelCNName;
	@FXML
	private TextField textFildCNName;
	@FXML
	private Label labelENName;
	@FXML
	private TextField textFiledENName;
	@FXML
	private Label labelFacility;
	@FXML
	private TextField textFiledFacility;
	@FXML
	private Button btnFacility;
	@FXML
	private Button btnSelected;
	@FXML
	private Button btnInsert;
	@FXML
	private Button btnSave;
	@FXML
	private CheckBox cbAppendFile;
	@FXML
	private ListView<CheckBox> listView;

	// �洢�豸��̬��
	private HashMap<String, HashMap<String, String>> facitys = new HashMap<>();

	// Event Listener on MenuItem.onAction
	@FXML
	public void onSetingClick(ActionEvent event) {
		if (!StageHelper.stages.containsKey("SettingStage")) {
			try {
				Parent root = FXMLLoader.load(getClass().getResource("Settings.fxml"));
				Scene scene = new Scene(root, 600, 400);
				Stage settingStage = new Stage();
				settingStage.setScene(scene);
				StageHelper.stages.put("SettingStage", settingStage);
				settingStage.setOnCloseRequest(ValueAxis -> {
					System.out.println("setting close");
					try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("config"))){
						bufferedWriter.write("IPBordInfo="+((TextField)scene.lookup("#textFildIPBordInfo")).getText());
						bufferedWriter.newLine();
						//OTHERS
					}catch (Exception e) {
						// TODO: handle exception
					}
				});
				settingStage.setAlwaysOnTop(true);
				settingStage.setResizable(false);
				settingStage.setTitle("�ļ��洢λ������");
				settingStage.show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}else{
			StageHelper.stages.get("SettingStage").show();
		}
	}

	// �����豸��̬�ļ�
	@FXML
	public void btnLoadClick(ActionEvent event) {
		FileChooser faciltyChooser = new FileChooser();
		faciltyChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All files", "*.*"),
				new FileChooser.ExtensionFilter("*.txt", "*.txt"));
		File facityFile = faciltyChooser.showOpenDialog(StageHelper.stages.get("MainStage"));
		if (null != facityFile) {
			try {
				this.textFildLoad.setText(facityFile.getCanonicalPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// ��ȡ�ļ�
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(facityFile))) {
			String data = null;
			while ((data = bufferedReader.readLine()) != null) {
				if ("".equals(data)) {
					continue;
				}
				int i = data.indexOf(",");
				System.out.println("i" + i);
				i = i > 0 ? i : data.length();
				String[] myvale = data.substring(0, i).split("/");
				System.out.println(Arrays.toString(myvale));
				int j = myvale[0].indexOf("-");
				j = j > 0 ? j : myvale[0].length();
				String key2 = myvale[0].substring(0, j);
				if (this.facitys.get(myvale[1]) == null) {
					HashMap<String, String> value = new HashMap<>();
					value.put(key2, data);
					this.facitys.put(myvale[1], value);
				} else {
					this.facitys.get(myvale[1]).put(key2, data);
				}

			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		this.btnFacility.setDisable(false);

	}

	// ѡ�������ļ��ı���λ��
	@FXML
	public void btnSavePathClick(ActionEvent event) {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		File direFile = directoryChooser.showDialog(StageHelper.stages.get("MainStage"));
		if (null != direFile) {
			this.textFildSave.setText(direFile.getAbsolutePath());
		}
		// �õ�λ��

	}

	// ��ѯ�豸��Ϣ
	// Event Listener on Button[#btnFacility].onAction
	@FXML
	public void btnFacilityClick(ActionEvent event) {
		String value = this.textFiledFacility.getText().trim();
		if ("".equals(value) || null == value) {
			return;
		}
		ObservableList<CheckBox> observableList = FXCollections.observableArrayList();
		String[] searchValue = value.split(";");
		for (String string : searchValue) {
			String[] sp = string.split("/");
			String key1 = sp[1];
			String key2 = sp[0].split("-")[0];
			String result = this.facitys.get(key1).get(key2);
			System.out.println(result);
			CheckBox checkBox = new CheckBox(result);
			checkBox.setSelected(true);
			observableList.add(checkBox);
		}
		this.listView.setItems(observableList);
		// TODO Autogenerated
	}

	@FXML
	public void btnSelectedClick(ActionEvent event) {

	}

	// Event Listener on Button[#btnInsert].onAction
	@FXML
	public void btnInsertClick(ActionEvent event) {
		// TODO Autogenerated
	}

	// Event Listener on Button[#btnSave].onAction
	@FXML
	public void btnSave(ActionEvent event) {
		// ((Label)StageHelper.stages.get("MainStage").getScene().lookup("#labelLoad")).setText("test");
		// TODO Autogenerated
	}

	// ��ʼ��Main.fxml��Ӧ�Ŀ���������̬���ɿռ��һЩ����
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.labelLoad.setText("�豸�ļ�");
		this.btnLoad.setText("����");
		this.labelSave.setText("�洢λ��");
		this.btnSavePath.setText("ѡ��");
		this.labelId.setText("Id");
		this.labelDescriptio.setText("description");
		this.labelCNName.setText("��������");
		this.labelENName.setText("Ӣ������");
		this.labelFacility.setText("�豸��̬");
		this.btnFacility.setText("��ѯ");
		this.btnFacility.setDisable(true);
		this.btnSelected.setText("ѡ��");
		this.btnSelected.setVisible(false);
		this.btnInsert.setText("����");
		this.btnSave.setText("����");
		this.cbAppendFile.setText("׷���ļ�");

		this.cbAppendFile.setTooltip(new Tooltip("����������е��ļ�����ѡ\'׷���ļ�\'���������ļ���ĩβ��ӣ��������ѡ�򸲸��Ժ��ļ���"));
		try(BufferedReader bufferedReader=new BufferedReader(new FileReader("confg"))){
			String data=bufferedReader.readLine();
			SettingHelper.IPBordInfo=data.substring(data.indexOf("=")+1);
		}catch (Exception e) {
			// TODO: handle exception
		}

	}
}
