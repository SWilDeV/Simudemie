/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.ui;

import ca.ulaval.glo2004.domain.CloseLinkDTO;
import ca.ulaval.glo2004.domain.CountryDTO;
import ca.ulaval.glo2004.domain.DiseaseDTO;
import ca.ulaval.glo2004.domain.HealthMesureDTO;
import ca.ulaval.glo2004.domain.IrregularForm;
import ca.ulaval.glo2004.domain.Link;
import ca.ulaval.glo2004.domain.LinkDTO;
import ca.ulaval.glo2004.domain.NotAllPopulationAssign;
import ca.ulaval.glo2004.domain.Population;
import ca.ulaval.glo2004.domain.Disease;
import ca.ulaval.glo2004.domain.RegionDTO;
import ca.ulaval.glo2004.domain.Utility;
import ca.ulaval.glo2004.domain.WorldController;
import ca.ulaval.glo2004.domain.WorldObserver;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author melanietremblay
 */
public class MainWindow extends javax.swing.JFrame implements WorldObserver {
    
    public DrawingPanel drawingPanel;
    public JFreeChart chart;
    public WorldController worldController;
    
    public List<Point> mousePoints = new ArrayList<>();
    public CountryDTO countrySelected = null;
    public int countrySelectedPointIndex = -1;
    
    public DiseaseDTO diseaseSelected = null;
    public enum Mode {Idle, AddCountry, AddCountryIrregular, ModifyCountry, AddLink, ModifyLink, Select, AddRegion};
    public Mode mode = Mode.Idle;
    private CountryDTO onHoverCountry = null; //Je sais que c'est pas bien, mais pour test, on va faire ca.
    private Point onHoverMousePosition = new Point(); //Je sais que c'est pas bien, mais pour test, on va faire ca.
    private final JFileChooser fileChooser = new JFileChooser();
    private final JFileChooser imageChooser = new JFileChooser();
    public XYSeries deathsNum = new XYSeries("Morts");
    public XYSeries infectedNum = new XYSeries("Infectés");
    public XYSeries nonInfectedNum = new XYSeries("Non Infectés");
 
    /**
     * Creates new form MainWindow
     */
    public MainWindow() throws CloneNotSupportedException {
        initComponents();
        
        this.worldController = new WorldController();
        
        drawingPanel = new DrawingPanel(this, jPanelDraw);
        jPanelDraw.add(drawingPanel);
        FileFilter imageFilter = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());
        imageChooser.setFileFilter(imageFilter);
        worldController.Subscribe(this);
        updateDiseasesUI();
        //updateCountryListUI();
    }
    
    @Override
    public void OnSimulationTick(int day, int deads, int infected, int PopTot) {
        System.err.println("Jour:" + day);

        deathsNum.add(day, deads);
        infectedNum.add(day, infected);
        int nonInfected = PopTot - deads - infected;
        nonInfectedNum.add(day, nonInfected);
        
        jSliderUndoRedo.setValue(worldController.GetUndoRedoSize());
        UpdateSimulationUI();
    }
    
    @Override
    public void OnRegionCreated() {
        try {
            AddUndoRedo();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void OnRegionUpdated() {
        try {
            AddUndoRedo();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void OnRegionDestroy() {
        try {
            AddUndoRedo();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void OnMesuresCreated() {
        try {
            AddUndoRedo();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void OnMesuresUpdated() {
        try {
            AddUndoRedo();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void OnMesuresDestroy() {
        try {
            AddUndoRedo();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void OnLinkCreated() {
        try {
            AddUndoRedo();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void OnLinksUpdated() {
        UpdateJLinkList();
    }
    
    @Override
    public void OnLinkDestroyed() {
        try {
            AddUndoRedo();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void OnCountryCreated(CountryDTO country) {
        UpdateJRegionList(country);
        updateCountryListUI();
        try {
            AddUndoRedo();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void OnCountryUpdated() {
        try {
            AddUndoRedo();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void OnCountryDestroy() {
        try {
            AddUndoRedo();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void OnSimulationStarted() {
        if(countrySelected != null){
            jSliderUndoRedo.setEnabled(false);
            UpdateJRegionList(countrySelected.Id);
            UpdateSliderUndoRedo();
        }
    }
    
    @Override
    public void OnSimulationReset() {
        UpdateSimulationUI();
        UpdateSliderUndoRedo();
        jSliderUndoRedo.setEnabled(true);
    }
    
    @Override
    public void OnSimulationStopped() {
        jSliderUndoRedo.setEnabled(true);
    }
    
    @Override
    public void OnSimulationUndoRedo() {
        UpdateSimulationUI();
        jSliderUndoRedo.setEnabled(true);
        UpdateSliderUndoRedo();
        
        if(countrySelected != null) {
            UpdateJRegionList(countrySelected.Id);
        }
        
        updateDiseasesUI();
    }
    
    @Override
    public void OnProjectLoaded() {
        UpdateSimulationUI();
        jSliderUndoRedo.setEnabled(true);
    }
    
    @Override
    public void OnDiseaseCreated(DiseaseDTO disease){
        jComboBoxDiseases.addItem(disease.getName());
    }
    
    @Override
    public void OnDiseaseUpdated(){
        
    }
    
    @Override
    public void OnDiseaseDestroyed(){
        
    }
    
    private void UpdateJLinkList() {
        DefaultListModel listModel = new DefaultListModel();
        for(LinkDTO l: worldController.GetLinks()) {
            String name = "";
            switch (l.LinkType) {
                case TERRESTRE:
                    name = "T - ";
                    break;
                case MARITIME:
                    name = "M - ";
                    break;
                default:
                    name = "A - ";
                    break;
            }
            
            String name1 = worldController.GetCountryDTO(l.Country1Id).Name;
            String name2 = worldController.GetCountryDTO(l.Country2Id).Name;
            String transmissionRate = String.valueOf(l.TransmissionRate);
            
            name += name1 + " <-> " + name2 + " -- " + transmissionRate;
            listModel.addElement(name);
        }
        jListLinks.setModel(listModel);
        jListLinksSim.setModel(listModel);
        drawingPanel.repaint();
    }
    
    private void UpdateJRegionList(UUID countryId) {
        CountryDTO c = worldController.GetCountryDTO(countryId);
        if(c != null) {
            UpdateJRegionList(c);
        }
    }
    
    private void UpdateJRegionList(CountryDTO country) {
        DefaultListModel listModel = new DefaultListModel();
        country.Regions.forEach(r -> {
            BigDecimal bdUp =new BigDecimal(r.PercentagePop * 100).setScale(2,RoundingMode.HALF_UP);
            listModel.addElement(String.format("%s | Pourcentage: %s | Population: %s", r.Name, bdUp, r.SubPopulation.totalPopulationDTO));
        });
        jListRegionsList.setModel(listModel);
        drawingPanel.repaint();
    }
    
    private void UpdateJMesureList(UUID countryId) {
        List<HealthMesureDTO> mesures = worldController.GetHealthMesures(countryId);
        if(mesures != null) { 
            DefaultListModel listMesuresModel = new DefaultListModel();

             mesures.forEach(m -> {
                 listMesuresModel.addElement(m.MesureName + " " + m.IsActive + " " + m.AdhesionRate + "% - " + m.threshold + "%");
             });
             jListMesures.setModel(listMesuresModel);
             drawingPanel.repaint();
        }
    }
    
    private void UpdateJListClosedLinks() {
        DefaultListModel listModel = new DefaultListModel();
        
        List<CloseLinkDTO> closedLinks = worldController.getClosedLinks();
        
        for(LinkDTO l: worldController.GetLinks()) {
            closedLinks.forEach(cl -> {
                if (l.Id == cl.ConcernedLink) {
                    String name = "";
                    switch (l.LinkType) {
                        case TERRESTRE:
                            name = "T - ";
                            break;
                        case MARITIME:
                            name = "M - ";
                            break;
                        default:
                            name = "A - ";
                            break;
                    }
            
                    String name1 = worldController.GetCountryDTO(l.Country1Id).Name;
                    String name2 = worldController.GetCountryDTO(l.Country2Id).Name;
            
                    name += name1 + " <-> " + name2 + " - FERME";
                    listModel.addElement(name);
                }
            });     
        }
        jListClosedLinks.setModel(listModel);
    }
    
    private void updateCountryListUI(){
        jComboBoxCountryPatientZero.removeAllItems();
        for(CountryDTO c: worldController.GetCountries()){
            jComboBoxCountryPatientZero.addItem(c.Name);
        }
    }
    
    private void updateDiseasesUI(){
        jTextFieldMortalityRate.setText("");
        jTextFieldReproductionRate.setText("");
        jTextFieldCuredRate.setText("");
        jTextFieldDiseaseName.setText("");
        
        
        jComboBoxDiseases.removeAllItems();
        for(DiseaseDTO d: worldController.getDiseasesDTO()) {
            jComboBoxDiseases.addItem(d.getName());
        }
        
        if(worldController.HasDesease()) {
            DiseaseDTO dis = worldController.GetDiseaseDTO();
            jTextFieldMortalityRate.setText(String.valueOf(dis.getMortalityRateDTO() * 100));
            jTextFieldReproductionRate.setText(String.valueOf(dis.getInfectionRateDTO() * 100));
            jTextFieldCuredRate.setText(String.valueOf(dis.getCureRateDTO() * 100));
            jTextFieldDiseaseName.setText(String.valueOf(dis.getName()));

            jComboBoxDiseases.getModel().setSelectedItem(jComboBoxDiseases.getItemAt(worldController.getSimulation().getCurrentDiseaseIndex()));
        }
    }
    
    private void UpdateSimulationUI() {
        Population pop = worldController.getWorld().getWorldPopulation();
        jLabelDayElapsed.setText(String.valueOf(worldController.GetElapsedDay()));
        jLabelDead.setText(String.valueOf(pop.getDeadPopulation()));
        jLabelCase.setText(String.valueOf(pop.getInfectedPopulation()));
        jLabelCured.setText(String.valueOf(pop.getNonInfectedPopulation()));
        jLabelPopMondial.setText(String.valueOf(pop.getTotalPopulation()));
        
        
        DiseaseDTO disease = worldController.getSimulation().GetDiseaseDTO();
//        DiseaseDTO disease = worldController.GetDiseaseDTO();
        jTextFieldMortalityRate.setText(String.valueOf(disease.getMortalityRateDTO() * 100));
        jTextFieldReproductionRate.setText(String.valueOf(disease.getInfectionRateDTO() * 100));
        jTextFieldCuredRate.setText(String.valueOf(disease.getCureRateDTO() * 100));
        
        double reproductionRate = disease.getInfectionRateDTO()/disease.getCureRateDTO();
        jLabelR.setText(String.valueOf(reproductionRate));
        jLabelMaxRRate.setText("(Valeur max: " + String.valueOf(reproductionRate) + ")");
        
        UpdateSliderUndoRedo();
        
        drawingPanel.repaint();
    }
    
    private void UpdateSliderUndoRedo() {
        jSliderUndoRedo.setMaximum(worldController.GetUndoRedoSize());
        jSliderUndoRedo.setValue(worldController.GetUndoRedoPosition());
        jLabelUndoRedoSliderText.setText(String.valueOf(jSliderUndoRedo.getValue()));
    }
    
    private void SelectCountry(Point mousePosition) {
        CountryDTO selected = Utility.SelectCountry(worldController.GetCountries(), mousePosition);
        SetSelectedCountry(selected);
    }
    
    private void SetSelectedCountry(CountryDTO country) {
        if(countrySelected != null) {
            worldController.UpdateSelectionStateCountry(countrySelected.Id, false);
        }
        
        if(country != null) {
            countrySelected = country;
            worldController.UpdateSelectionStateCountry(countrySelected.Id, true);
            UpdateJMesureList(countrySelected.Id);
            drawingPanel.repaint();
        } else {
            countrySelected = null;
        }
        
        countrySelectedPointIndex = -1;
    }
    
    private void AddUndoRedo() throws CloneNotSupportedException {
        worldController.AddUndoRedo();
        UpdateJLinkList();
        UpdateSimulationUI();
        
        jSliderUndoRedo.setValue(worldController.GetUndoRedoSize());
        jLabelUndoRedoSliderText.setText(String.valueOf(jSliderUndoRedo.getValue()));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroupConception = new javax.swing.ButtonGroup();
        buttonGroupSimulation = new javax.swing.ButtonGroup();
        jLabelTitle = new javax.swing.JLabel();
        jScrollPaneMap = new javax.swing.JScrollPane();
        jPanelDraw = new javax.swing.JPanel();
        jTabbedMainPane = new javax.swing.JTabbedPane();
        jPanelConception = new javax.swing.JPanel();
        jToggleBtnModifyCountry = new javax.swing.JToggleButton();
        jToggleBtnAddLink = new javax.swing.JToggleButton();
        jPanelConceptionOptions = new javax.swing.JPanel();
        jPanelBlank = new javax.swing.JPanel();
        jPanelCountry = new javax.swing.JPanel();
        jButtonDeleteCountry = new javax.swing.JButton();
        jLabelModCountryName = new javax.swing.JLabel();
        jLabelModCountryPop = new javax.swing.JLabel();
        jTextFieldCountryName = new javax.swing.JTextField();
        jTextFieldCountryPop = new javax.swing.JTextField();
        jPanelRegionOptions = new javax.swing.JPanel();
        jScrollPaneRegionsList = new javax.swing.JScrollPane();
        jListRegionsList = new javax.swing.JList<>();
        jLabelTitleRegionList = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jButtonRemoveRegion = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jTextFieldPercentageAddRegion = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldRegionName = new javax.swing.JTextField();
        jLabelTitleRegionName = new javax.swing.JLabel();
        jButtonAddRegion = new javax.swing.JButton();
        jButtonModifyRegion = new javax.swing.JButton();
        jButtonCreateRegularCountry = new javax.swing.JButton();
        jButtonCreateIrregularCountry = new javax.swing.JButton();
        jLabelTitleEditionRegion = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        jLabelTitleCreationEdtionPays = new javax.swing.JLabel();
        jPanelLink = new javax.swing.JPanel();
        jComboBoxAddLink = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jScrollPaneLinks = new javax.swing.JScrollPane();
        jListLinks = new javax.swing.JList<>();
        jButtonDeleteLink = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jLabelModifyLinkTrans = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel15 = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        jLabel16 = new javax.swing.JLabel();
        jButtonApplyAllLinksTravelRate = new javax.swing.JButton();
        jLabelRegionLink = new javax.swing.JLabel();
        jTextFieldRegionLinkTrans = new javax.swing.JTextField();
        jTextFieldPortTransRate = new javax.swing.JTextField();
        jTextFieldBorderTransRate = new javax.swing.JTextField();
        jTextFieldAirportTransRate = new javax.swing.JTextField();
        jTextFieldLinkTransRate = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jButtonResetZoom = new javax.swing.JButton();
        jPanelSimulation = new javax.swing.JPanel();
        jBtnReset = new javax.swing.JButton();
        jBtnPause = new javax.swing.JButton();
        jBtnPlay = new javax.swing.JButton();
        jLabelTitleCase = new javax.swing.JLabel();
        jLabelCase = new javax.swing.JLabel();
        jLabelTitleDead = new javax.swing.JLabel();
        jLabelDead = new javax.swing.JLabel();
        jLabelTitleCured = new javax.swing.JLabel();
        jLabelPopMondial = new javax.swing.JLabel();
        jLabelTitleDayElapsed = new javax.swing.JLabel();
        jLabelDayElapsed = new javax.swing.JLabel();
        jButtonBacktrack = new javax.swing.JButton();
        jButtonForward = new javax.swing.JButton();
        jLabelTimeLapse = new javax.swing.JLabel();
        jTextFieldSimulationTimeStep = new javax.swing.JTextField();
        jTabbedPaneSimulationOptions = new javax.swing.JTabbedPane();
        jPanelDeseaseParams = new javax.swing.JPanel();
        jPanelMortalityRate = new javax.swing.JPanel();
        jLabelTitleMortalityRate = new javax.swing.JLabel();
        jTextFieldMortalityRate = new javax.swing.JTextField();
        jPanelReproductionRate = new javax.swing.JPanel();
        jLabelTitleReproductionRate = new javax.swing.JLabel();
        jTextFieldReproductionRate = new javax.swing.JTextField();
        jPanelCuredRate = new javax.swing.JPanel();
        jLabelTitleCuredRate = new javax.swing.JLabel();
        jTextFieldCuredRate = new javax.swing.JTextField();
        jButtonApplyDisease = new javax.swing.JButton();
        jComboBoxDiseases = new javax.swing.JComboBox<>();
        jLabelTitleDeseaseName = new javax.swing.JLabel();
        jTextFieldDiseaseName = new javax.swing.JTextField();
        jButtonSaveNewDisease = new javax.swing.JButton();
        jButtonDeleteDisease = new javax.swing.JButton();
        jLabelReproductionRate = new javax.swing.JLabel();
        jLabelR = new javax.swing.JLabel();
        jPanelNumberOfZeroPatients = new javax.swing.JPanel();
        jLabelTitleNumberOfZeroPatients = new javax.swing.JLabel();
        jTextFieldNumberOfZeroPatients = new javax.swing.JTextField();
        jLabelSelectionCountryPatientZero = new javax.swing.JLabel();
        jComboBoxCountryPatientZero = new javax.swing.JComboBox<>();
        jButtonApplyPatientZero = new javax.swing.JButton();
        jPanelFermetureLiens = new javax.swing.JPanel();
        jLabelLinksSimTitle = new javax.swing.JLabel();
        jTextFieldCloseLinkAR = new javax.swing.JTextField();
        jLabelCloseLinkAR = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jTextFieldCloseLinkThreshold = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListLinksSim = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();
        jButtonAddCloseLink = new javax.swing.JButton();
        jScrollPaneClosedLinks = new javax.swing.JScrollPane();
        jListClosedLinks = new javax.swing.JList<>();
        jButtonModifyCloseLink = new javax.swing.JButton();
        jButtonDeleteCloseLink = new javax.swing.JButton();
        jPanelStatistics = new javax.swing.JPanel();
        jButtonCreateGraphic = new javax.swing.JButton();
        jPanelHealthMesures = new javax.swing.JPanel();
        jLabelMesureName = new javax.swing.JLabel();
        jTextFieldMesureName = new javax.swing.JTextField();
        jLabelAdhesionRate = new javax.swing.JLabel();
        jTextFieldAdhesionRate = new javax.swing.JTextField();
        jCheckBoxActiveMesure = new javax.swing.JCheckBox();
        jScrollPaneOtherMeasures = new javax.swing.JScrollPane();
        jListMesures = new javax.swing.JList<>();
        jButtonAddMesure = new javax.swing.JButton();
        jButtonDeleteMesure = new javax.swing.JButton();
        jLabelThreshold = new javax.swing.JLabel();
        jTextFieldThreshold = new javax.swing.JTextField();
        jLabelEffetTransmission = new javax.swing.JLabel();
        jLabelEffectReproductionRate = new javax.swing.JLabel();
        jTextFieldEffectTransmission = new javax.swing.JTextField();
        jTextFieldEffectReproductionRate = new javax.swing.JTextField();
        jLabelMaxRRate = new javax.swing.JLabel();
        jLabelTitlePopMondial = new javax.swing.JLabel();
        jLabelCured = new javax.swing.JLabel();
        jBtnChangeSimulationTimeStep = new javax.swing.JButton();
        jLabelDiseaseReminderName = new javax.swing.JLabel();
        jPanelLegend = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jButtonScreenShotWorld = new javax.swing.JButton();
        jLabelUndoRedoSliderText = new javax.swing.JLabel();
        jSliderUndoRedo = new javax.swing.JSlider();
        jButtonChangeBackgroundImage = new javax.swing.JButton();
        jMainMenuBar = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItemNew = new javax.swing.JMenuItem();
        jMenuItemSave = new javax.swing.JMenuItem();
        jMenuItemLoad = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItemUndo = new javax.swing.JMenuItem();
        jMenuItemRedo = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Simudémie");

        jLabelTitle.setFont(new java.awt.Font("Academy Engraved LET", 0, 36)); // NOI18N
        jLabelTitle.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabelTitle.setText("Simudémie");

        jScrollPaneMap.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPaneMap.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        jPanelDraw.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jPanelDrawMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jPanelDrawMouseMoved(evt);
            }
        });
        jPanelDraw.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                jPanelDrawMouseWheelMoved(evt);
            }
        });
        jPanelDraw.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanelDrawMousePressed(evt);
            }
        });
        jScrollPaneMap.setViewportView(jPanelDraw);

        jTabbedMainPane.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        buttonGroupConception.add(jToggleBtnModifyCountry);
        jToggleBtnModifyCountry.setText("Pays");
        jToggleBtnModifyCountry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleBtnModifyCountryActionPerformed(evt);
            }
        });

        buttonGroupConception.add(jToggleBtnAddLink);
        jToggleBtnAddLink.setText("Liens");
        jToggleBtnAddLink.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleBtnAddLinkActionPerformed(evt);
            }
        });

        jPanelConceptionOptions.setLayout(new java.awt.CardLayout());

        javax.swing.GroupLayout jPanelBlankLayout = new javax.swing.GroupLayout(jPanelBlank);
        jPanelBlank.setLayout(jPanelBlankLayout);
        jPanelBlankLayout.setHorizontalGroup(
            jPanelBlankLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 368, Short.MAX_VALUE)
        );
        jPanelBlankLayout.setVerticalGroup(
            jPanelBlankLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 601, Short.MAX_VALUE)
        );

        jPanelConceptionOptions.add(jPanelBlank, "card6");

        jButtonDeleteCountry.setText("Supprimer le pays");
        jButtonDeleteCountry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteCountryActionPerformed(evt);
            }
        });

        jLabelModCountryName.setText("Nom:");

        jLabelModCountryPop.setText("Population:");

        jTextFieldCountryName.setText("Nom");
        jTextFieldCountryName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldCountryNameKeyPressed(evt);
            }
        });

        jTextFieldCountryPop.setText("10000");
        jTextFieldCountryPop.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldCountryPopKeyPressed(evt);
            }
        });

        jListRegionsList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListRegionsListValueChanged(evt);
            }
        });
        jScrollPaneRegionsList.setViewportView(jListRegionsList);

        jLabelTitleRegionList.setText("Regions");

        jButtonRemoveRegion.setText("Supprimer la region");
        jButtonRemoveRegion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemoveRegionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelRegionOptionsLayout = new javax.swing.GroupLayout(jPanelRegionOptions);
        jPanelRegionOptions.setLayout(jPanelRegionOptionsLayout);
        jPanelRegionOptionsLayout.setHorizontalGroup(
            jPanelRegionOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelRegionOptionsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelRegionOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPaneRegionsList)
                    .addComponent(jSeparator2)
                    .addGroup(jPanelRegionOptionsLayout.createSequentialGroup()
                        .addComponent(jLabelTitleRegionList)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jButtonRemoveRegion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanelRegionOptionsLayout.setVerticalGroup(
            jPanelRegionOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelRegionOptionsLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelTitleRegionList)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPaneRegionsList, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonRemoveRegion)
                .addContainerGap())
        );

        jTextFieldPercentageAddRegion.setText("25");

        jLabel2.setText("Pourcentage");

        jTextFieldRegionName.setText("Region");

        jLabelTitleRegionName.setText("Nom");

        jButtonAddRegion.setText("Ajouter la region");
        jButtonAddRegion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddRegionActionPerformed(evt);
            }
        });

        jButtonModifyRegion.setText("Modifier la region");
        jButtonModifyRegion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModifyRegionActionPerformed(evt);
            }
        });

        jButtonCreateRegularCountry.setText("Regulier");
        jButtonCreateRegularCountry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCreateRegularCountryActionPerformed(evt);
            }
        });

        jButtonCreateIrregularCountry.setText("Irregulier");
        jButtonCreateIrregularCountry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCreateIrregularCountryActionPerformed(evt);
            }
        });

        jLabelTitleEditionRegion.setText("Edition des regions");

        jLabelTitleCreationEdtionPays.setText("Creation/Edition des pays");

        javax.swing.GroupLayout jPanelCountryLayout = new javax.swing.GroupLayout(jPanelCountry);
        jPanelCountry.setLayout(jPanelCountryLayout);
        jPanelCountryLayout.setHorizontalGroup(
            jPanelCountryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCountryLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelCountryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelCountryLayout.createSequentialGroup()
                        .addComponent(jButtonDeleteCountry, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addComponent(jPanelRegionOptions, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelCountryLayout.createSequentialGroup()
                        .addGroup(jPanelCountryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabelTitleRegionName))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanelCountryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextFieldPercentageAddRegion)
                            .addComponent(jTextFieldRegionName, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(30, 30, 30))
                    .addComponent(jSeparator1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelCountryLayout.createSequentialGroup()
                        .addComponent(jLabelTitleEditionRegion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanelCountryLayout.createSequentialGroup()
                        .addGroup(jPanelCountryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelCountryLayout.createSequentialGroup()
                                .addComponent(jLabelModCountryName)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldCountryName, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(25, 25, 25)
                                .addComponent(jLabelModCountryPop)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldCountryPop, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanelCountryLayout.createSequentialGroup()
                                .addComponent(jButtonCreateRegularCountry, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButtonCreateIrregularCountry, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelCountryLayout.createSequentialGroup()
                        .addComponent(jSeparator6)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelCountryLayout.createSequentialGroup()
                        .addGroup(jPanelCountryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonModifyRegion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonAddRegion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelCountryLayout.createSequentialGroup()
                        .addComponent(jLabelTitleCreationEdtionPays, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        jPanelCountryLayout.setVerticalGroup(
            jPanelCountryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCountryLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelTitleCreationEdtionPays)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelCountryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelModCountryName)
                    .addComponent(jTextFieldCountryName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelModCountryPop)
                    .addComponent(jTextFieldCountryPop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelCountryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonCreateRegularCountry)
                    .addComponent(jButtonCreateIrregularCountry))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonDeleteCountry)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 148, Short.MAX_VALUE)
                .addComponent(jLabelTitleEditionRegion)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelCountryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldRegionName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelTitleRegionName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelCountryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldPercentageAddRegion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonAddRegion)
                .addGap(1, 1, 1)
                .addComponent(jButtonModifyRegion)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelRegionOptions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanelConceptionOptions.add(jPanelCountry, "card2");

        jComboBoxAddLink.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Terrestre", "Maritime", "Aérien" }));

        jLabel6.setText("Ajouter");

        jListLinks.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListLinksValueChanged(evt);
            }
        });
        jScrollPaneLinks.setViewportView(jListLinks);

        jButtonDeleteLink.setText("Supprimer lien");
        jButtonDeleteLink.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteLinkActionPerformed(evt);
            }
        });

        jLabel10.setText("Type");

        jLabelModifyLinkTrans.setText("Taux de transmision des liens");

        jLabel12.setText("Terrestre (%)");

        jLabel13.setText("Aerien (%)");

        jLabel14.setText("Maritime (%)");

        jLabel15.setText("Liens");

        jLabel16.setText("Modifier le taux de transmision d'un lien spécifique");

        jButtonApplyAllLinksTravelRate.setText("Mettre à jour");
        jButtonApplyAllLinksTravelRate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonApplyAllLinksTravelRateActionPerformed(evt);
            }
        });

        jLabelRegionLink.setText("Régional (%)");

        jTextFieldRegionLinkTrans.setText("0.0");

        jTextFieldPortTransRate.setText("0.0");

        jTextFieldBorderTransRate.setText("0.0");

        jTextFieldAirportTransRate.setText("0.0");

        jTextFieldLinkTransRate.setText("0.0");
        jTextFieldLinkTransRate.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldLinkTransRateKeyPressed(evt);
            }
        });

        jLabel11.setText("(Sélectionner, modifier et cliquer Enter)");

        javax.swing.GroupLayout jPanelLinkLayout = new javax.swing.GroupLayout(jPanelLink);
        jPanelLink.setLayout(jPanelLinkLayout);
        jPanelLinkLayout.setHorizontalGroup(
            jPanelLinkLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLinkLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelLinkLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelLinkLayout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(54, 54, 54)
                        .addComponent(jComboBoxAddLink, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPaneLinks, javax.swing.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE)
                    .addComponent(jButtonDeleteLink, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelModifyLinkTrans)
                    .addGroup(jPanelLinkLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jButtonApplyAllLinksTravelRate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabelRegionLink, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelLinkLayout.createSequentialGroup()
                        .addGroup(jPanelLinkLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanelLinkLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextFieldAirportTransRate, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                            .addComponent(jTextFieldBorderTransRate)))
                    .addComponent(jSeparator7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelLinkLayout.createSequentialGroup()
                        .addGroup(jPanelLinkLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addGroup(jPanelLinkLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel11)))
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldLinkTransRate))
                    .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelLinkLayout.createSequentialGroup()
                        .addGap(254, 254, 254)
                        .addGroup(jPanelLinkLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextFieldRegionLinkTrans, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                            .addComponent(jTextFieldPortTransRate)))
                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanelLinkLayout.setVerticalGroup(
            jPanelLinkLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLinkLayout.createSequentialGroup()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addGroup(jPanelLinkLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxAddLink, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addGap(27, 27, 27)
                .addComponent(jLabelModifyLinkTrans)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelLinkLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jTextFieldBorderTransRate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelLinkLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jTextFieldAirportTransRate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelLinkLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jTextFieldPortTransRate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(jPanelLinkLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelRegionLink)
                    .addComponent(jTextFieldRegionLinkTrans, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonApplyAllLinksTravelRate, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelLinkLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldLinkTransRate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPaneLinks, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonDeleteLink)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jPanelConceptionOptions.add(jPanelLink, "card5");

        jButtonResetZoom.setText("Reset zoom");
        jButtonResetZoom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonResetZoomActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelConceptionLayout = new javax.swing.GroupLayout(jPanelConception);
        jPanelConception.setLayout(jPanelConceptionLayout);
        jPanelConceptionLayout.setHorizontalGroup(
            jPanelConceptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelConceptionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelConceptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jToggleBtnModifyCountry, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
                    .addComponent(jToggleBtnAddLink, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonResetZoom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(18, Short.MAX_VALUE))
            .addGroup(jPanelConceptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelConceptionLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanelConceptionOptions, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanelConceptionLayout.setVerticalGroup(
            jPanelConceptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelConceptionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToggleBtnModifyCountry)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToggleBtnAddLink)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonResetZoom)
                .addContainerGap(612, Short.MAX_VALUE))
            .addGroup(jPanelConceptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelConceptionLayout.createSequentialGroup()
                    .addContainerGap(108, Short.MAX_VALUE)
                    .addComponent(jPanelConceptionOptions, javax.swing.GroupLayout.PREFERRED_SIZE, 601, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );

        jTabbedMainPane.addTab("Conception", jPanelConception);

        jBtnReset.setText("Reset");
        jBtnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnResetActionPerformed(evt);
            }
        });

        jBtnPause.setText("Pause");
        jBtnPause.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnPauseActionPerformed(evt);
            }
        });

        jBtnPlay.setText("Démarrer");
        jBtnPlay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnPlayActionPerformed(evt);
            }
        });

        jLabelTitleCase.setText("Infectés:");

        jLabelCase.setText("-");

        jLabelTitleDead.setText("Morts:");

        jLabelDead.setText("-");

        jLabelTitleCured.setText("Pop. Saine:");

        jLabelPopMondial.setText("-");

        jLabelTitleDayElapsed.setText("Jour:");

        jLabelDayElapsed.setText("-");

        jButtonBacktrack.setText("<<");
        jButtonBacktrack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBacktrackActionPerformed(evt);
            }
        });

        jButtonForward.setText(">>");
        jButtonForward.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonForwardActionPerformed(evt);
            }
        });

        jLabelTimeLapse.setText("Durée d'un pas de temps (s):");

        jTextFieldSimulationTimeStep.setText("1");

        jPanelMortalityRate.setLayout(new java.awt.GridLayout(1, 0));

        jLabelTitleMortalityRate.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabelTitleMortalityRate.setText("Taux mortalite (%)");
        jPanelMortalityRate.add(jLabelTitleMortalityRate);

        jTextFieldMortalityRate.setText("2");
        jTextFieldMortalityRate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldMortalityRateActionPerformed(evt);
            }
        });
        jPanelMortalityRate.add(jTextFieldMortalityRate);

        jPanelReproductionRate.setLayout(new java.awt.GridLayout(1, 0));

        jLabelTitleReproductionRate.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabelTitleReproductionRate.setText("Taux infection (%)");
        jPanelReproductionRate.add(jLabelTitleReproductionRate);

        jTextFieldReproductionRate.setText("15");
        jPanelReproductionRate.add(jTextFieldReproductionRate);

        jPanelCuredRate.setLayout(new java.awt.GridLayout(1, 0));

        jLabelTitleCuredRate.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabelTitleCuredRate.setText("Taux guerison (%)");
        jPanelCuredRate.add(jLabelTitleCuredRate);

        jTextFieldCuredRate.setText("4");
        jPanelCuredRate.add(jTextFieldCuredRate);

        jButtonApplyDisease.setText("Appliquer");
        jButtonApplyDisease.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonApplyDiseaseActionPerformed(evt);
            }
        });

        jComboBoxDiseases.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxDiseasesActionPerformed(evt);
            }
        });
        jComboBoxDiseases.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jComboBoxDiseasesPropertyChange(evt);
            }
        });

        jLabelTitleDeseaseName.setText("Nom");

        jTextFieldDiseaseName.setText("Nom maladie");

        jButtonSaveNewDisease.setText("Savegarder");
        jButtonSaveNewDisease.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveNewDiseaseActionPerformed(evt);
            }
        });

        jButtonDeleteDisease.setText("Supprimer");
        jButtonDeleteDisease.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteDiseaseActionPerformed(evt);
            }
        });

        jLabelReproductionRate.setText("Taux de reproduction (calculé): ");

        jPanelNumberOfZeroPatients.setLayout(new java.awt.GridLayout(1, 0));

        jLabelTitleNumberOfZeroPatients.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabelTitleNumberOfZeroPatients.setText("Nombre de patient(s) zéro(s)");
        jPanelNumberOfZeroPatients.add(jLabelTitleNumberOfZeroPatients);

        jTextFieldNumberOfZeroPatients.setText("1");
        jTextFieldNumberOfZeroPatients.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldNumberOfZeroPatientsActionPerformed(evt);
            }
        });
        jPanelNumberOfZeroPatients.add(jTextFieldNumberOfZeroPatients);

        jLabelSelectionCountryPatientZero.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabelSelectionCountryPatientZero.setText("Selection du pays avec patient(s) zéro(s)");

        jComboBoxCountryPatientZero.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Veuillez sélectionner un pays" }));
        jComboBoxCountryPatientZero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxCountryPatientZeroActionPerformed(evt);
            }
        });
        jComboBoxCountryPatientZero.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jComboBoxCountryPatientZeroPropertyChange(evt);
            }
        });

        jButtonApplyPatientZero.setText("Appliquer");
        jButtonApplyPatientZero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonApplyPatientZeroActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelDeseaseParamsLayout = new javax.swing.GroupLayout(jPanelDeseaseParams);
        jPanelDeseaseParams.setLayout(jPanelDeseaseParamsLayout);
        jPanelDeseaseParamsLayout.setHorizontalGroup(
            jPanelDeseaseParamsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDeseaseParamsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelDeseaseParamsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanelDeseaseParamsLayout.createSequentialGroup()
                        .addComponent(jLabelReproductionRate, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabelR, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelDeseaseParamsLayout.createSequentialGroup()
                        .addComponent(jButtonSaveNewDisease, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonDeleteDisease, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jComboBoxDiseases, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelDeseaseParamsLayout.createSequentialGroup()
                        .addComponent(jLabelTitleDeseaseName)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldDiseaseName))
                    .addComponent(jPanelMortalityRate, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelReproductionRate, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelCuredRate, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonApplyDisease, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBoxCountryPatientZero, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelSelectionCountryPatientZero, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelNumberOfZeroPatients, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonApplyPatientZero, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelDeseaseParamsLayout.setVerticalGroup(
            jPanelDeseaseParamsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDeseaseParamsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jComboBoxDiseases, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelDeseaseParamsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelTitleDeseaseName)
                    .addComponent(jTextFieldDiseaseName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addComponent(jPanelMortalityRate, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jPanelReproductionRate, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jPanelCuredRate, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelDeseaseParamsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanelDeseaseParamsLayout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jLabelReproductionRate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabelR, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonApplyDisease, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelDeseaseParamsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonDeleteDisease, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonSaveNewDisease, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelSelectionCountryPatientZero, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBoxCountryPatientZero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelNumberOfZeroPatients, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonApplyPatientZero, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(243, 243, 243))
        );

        jTabbedPaneSimulationOptions.addTab("Paramètres de la maladie", jPanelDeseaseParams);

        jLabelLinksSimTitle.setText("Fermeture des liens");

        jTextFieldCloseLinkAR.setText("0.0");

        jLabelCloseLinkAR.setText("Taux d'adhésion (%):");

        jLabel18.setText("Seuil (%):");

        jTextFieldCloseLinkThreshold.setText("0.0");

        jListLinksSim.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListLinksSimValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jListLinksSim);

        jLabel1.setText("Sélectionner le lien:");

        jButtonAddCloseLink.setText("Ajouter fermeture");
        jButtonAddCloseLink.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddCloseLinkActionPerformed(evt);
            }
        });

        jScrollPaneClosedLinks.setViewportView(jListClosedLinks);

        jButtonModifyCloseLink.setText("Modifier");

        jButtonDeleteCloseLink.setText("Retirer");
        jButtonDeleteCloseLink.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteCloseLinkActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelFermetureLiensLayout = new javax.swing.GroupLayout(jPanelFermetureLiens);
        jPanelFermetureLiens.setLayout(jPanelFermetureLiensLayout);
        jPanelFermetureLiensLayout.setHorizontalGroup(
            jPanelFermetureLiensLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelFermetureLiensLayout.createSequentialGroup()
                .addGroup(jPanelFermetureLiensLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jButtonAddCloseLink, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPaneClosedLinks, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanelFermetureLiensLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanelFermetureLiensLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelFermetureLiensLayout.createSequentialGroup()
                                .addComponent(jLabelCloseLinkAR)
                                .addGroup(jPanelFermetureLiensLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanelFermetureLiensLayout.createSequentialGroup()
                                        .addGap(99, 99, 99)
                                        .addComponent(jTextFieldCloseLinkAR, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE))
                                    .addGroup(jPanelFermetureLiensLayout.createSequentialGroup()
                                        .addGap(32, 32, 32)
                                        .addComponent(jLabel18)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jTextFieldCloseLinkThreshold))))
                            .addGroup(jPanelFermetureLiensLayout.createSequentialGroup()
                                .addGroup(jPanelFermetureLiensLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelLinksSimTitle)
                                    .addComponent(jLabel1))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanelFermetureLiensLayout.createSequentialGroup()
                        .addComponent(jButtonModifyCloseLink, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonDeleteCloseLink, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanelFermetureLiensLayout.setVerticalGroup(
            jPanelFermetureLiensLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelFermetureLiensLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelLinksSimTitle)
                .addGap(33, 33, 33)
                .addGroup(jPanelFermetureLiensLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelCloseLinkAR)
                    .addComponent(jTextFieldCloseLinkAR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelFermetureLiensLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldCloseLinkThreshold, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonAddCloseLink)
                .addGap(18, 18, 18)
                .addComponent(jScrollPaneClosedLinks, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelFermetureLiensLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonModifyCloseLink)
                    .addComponent(jButtonDeleteCloseLink))
                .addContainerGap(147, Short.MAX_VALUE))
        );

        jTabbedPaneSimulationOptions.addTab("Fermeture Liens", jPanelFermetureLiens);

        jButtonCreateGraphic.setText("Créer graphique");
        jButtonCreateGraphic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCreateGraphicActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelStatisticsLayout = new javax.swing.GroupLayout(jPanelStatistics);
        jPanelStatistics.setLayout(jPanelStatisticsLayout);
        jPanelStatisticsLayout.setHorizontalGroup(
            jPanelStatisticsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelStatisticsLayout.createSequentialGroup()
                .addContainerGap(105, Short.MAX_VALUE)
                .addComponent(jButtonCreateGraphic)
                .addGap(103, 103, 103))
        );
        jPanelStatisticsLayout.setVerticalGroup(
            jPanelStatisticsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelStatisticsLayout.createSequentialGroup()
                .addContainerGap(501, Short.MAX_VALUE)
                .addComponent(jButtonCreateGraphic)
                .addGap(40, 40, 40))
        );

        jTabbedPaneSimulationOptions.addTab("Statistiques", jPanelStatistics);

        jLabelMesureName.setText("Nom mesure:");

        jTextFieldMesureName.setText("Nom");

        jLabelAdhesionRate.setText("Taux d'adhésion (%):");

        jTextFieldAdhesionRate.setText("80");

        jCheckBoxActiveMesure.setText("Mesure active");
        jCheckBoxActiveMesure.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxActiveMesureActionPerformed(evt);
            }
        });

        jScrollPaneOtherMeasures.setViewportView(jListMesures);

        jButtonAddMesure.setText("Ajouter mesure");
        jButtonAddMesure.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddMesureActionPerformed(evt);
            }
        });

        jButtonDeleteMesure.setText("Supprimer mesure");
        jButtonDeleteMesure.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteMesureActionPerformed(evt);
            }
        });

        jLabelThreshold.setText("Seuil (%):");

        jTextFieldThreshold.setText("10");

        jLabelEffetTransmission.setText("Diminution du taux de transmission (%): ");

        jLabelEffectReproductionRate.setText("Diminution du taux de reproduction:");

        jTextFieldEffectTransmission.setText("0.0");

        jTextFieldEffectReproductionRate.setText("0.0");

        javax.swing.GroupLayout jPanelHealthMesuresLayout = new javax.swing.GroupLayout(jPanelHealthMesures);
        jPanelHealthMesures.setLayout(jPanelHealthMesuresLayout);
        jPanelHealthMesuresLayout.setHorizontalGroup(
            jPanelHealthMesuresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelHealthMesuresLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelAdhesionRate)
                .addGap(25, 25, 25)
                .addComponent(jTextFieldAdhesionRate))
            .addGroup(jPanelHealthMesuresLayout.createSequentialGroup()
                .addGroup(jPanelHealthMesuresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonDeleteMesure, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPaneOtherMeasures, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanelHealthMesuresLayout.createSequentialGroup()
                        .addComponent(jCheckBoxActiveMesure)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelThreshold, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextFieldThreshold, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelHealthMesuresLayout.createSequentialGroup()
                        .addComponent(jLabelMesureName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                        .addComponent(jTextFieldMesureName, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelHealthMesuresLayout.createSequentialGroup()
                        .addComponent(jLabelEffetTransmission, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldEffectTransmission, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelHealthMesuresLayout.createSequentialGroup()
                        .addGroup(jPanelHealthMesuresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelEffectReproductionRate)
                            .addComponent(jLabelMaxRRate, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextFieldEffectReproductionRate, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButtonAddMesure, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanelHealthMesuresLayout.setVerticalGroup(
            jPanelHealthMesuresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelHealthMesuresLayout.createSequentialGroup()
                .addContainerGap(154, Short.MAX_VALUE)
                .addGroup(jPanelHealthMesuresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelMesureName)
                    .addComponent(jTextFieldMesureName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelHealthMesuresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelEffetTransmission)
                    .addComponent(jTextFieldEffectTransmission, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelHealthMesuresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldEffectReproductionRate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelHealthMesuresLayout.createSequentialGroup()
                        .addComponent(jLabelEffectReproductionRate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelMaxRRate, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(17, 17, 17)
                .addGroup(jPanelHealthMesuresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelAdhesionRate)
                    .addComponent(jTextFieldAdhesionRate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelHealthMesuresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBoxActiveMesure)
                    .addComponent(jLabelThreshold)
                    .addComponent(jTextFieldThreshold, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonAddMesure)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonDeleteMesure)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPaneOtherMeasures, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(62, 62, 62))
        );

        jTabbedPaneSimulationOptions.addTab("Mesures sanitaires", jPanelHealthMesures);

        jLabelTitlePopMondial.setText("Pop. Mondiale:");

        jLabelCured.setText("-");

        jBtnChangeSimulationTimeStep.setText("Changer temps");
        jBtnChangeSimulationTimeStep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnChangeSimulationTimeStepActionPerformed(evt);
            }
        });

        jLabelDiseaseReminderName.setText("ReminderDiseaseName");

        javax.swing.GroupLayout jPanelSimulationLayout = new javax.swing.GroupLayout(jPanelSimulation);
        jPanelSimulation.setLayout(jPanelSimulationLayout);
        jPanelSimulationLayout.setHorizontalGroup(
            jPanelSimulationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSimulationLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelSimulationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelSimulationLayout.createSequentialGroup()
                        .addGroup(jPanelSimulationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanelSimulationLayout.createSequentialGroup()
                                .addGroup(jPanelSimulationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelSimulationLayout.createSequentialGroup()
                                        .addComponent(jLabelTitleCase)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabelCase, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                    .addGroup(jPanelSimulationLayout.createSequentialGroup()
                                        .addComponent(jBtnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(13, 13, 13)))
                                .addGroup(jPanelSimulationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanelSimulationLayout.createSequentialGroup()
                                        .addComponent(jBtnPlay)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jBtnPause))
                                    .addGroup(jPanelSimulationLayout.createSequentialGroup()
                                        .addComponent(jLabelTitleDead)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabelDead, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabelTitleCured))))
                            .addGroup(jPanelSimulationLayout.createSequentialGroup()
                                .addComponent(jLabelTitlePopMondial)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelPopMondial, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(11, 11, 11)
                        .addComponent(jLabelCured, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanelSimulationLayout.createSequentialGroup()
                        .addComponent(jLabelTitleDayElapsed)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelDayElapsed, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonBacktrack)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonForward)
                        .addGap(18, 18, 18)
                        .addComponent(jLabelDiseaseReminderName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanelSimulationLayout.createSequentialGroup()
                        .addGroup(jPanelSimulationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelSimulationLayout.createSequentialGroup()
                                .addComponent(jLabelTimeLapse)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextFieldSimulationTimeStep, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jBtnChangeSimulationTimeStep))
                            .addComponent(jTabbedPaneSimulationOptions, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanelSimulationLayout.setVerticalGroup(
            jPanelSimulationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSimulationLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelSimulationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelTitleDayElapsed, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelDayElapsed, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonBacktrack)
                    .addComponent(jButtonForward)
                    .addComponent(jLabelDiseaseReminderName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSimulationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelTimeLapse)
                    .addComponent(jTextFieldSimulationTimeStep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBtnChangeSimulationTimeStep))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSimulationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBtnPlay)
                    .addComponent(jBtnPause)
                    .addComponent(jBtnReset))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSimulationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelTitleCase, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelCase, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelTitleDead, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelDead, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelTitleCured, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelCured, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSimulationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelTitlePopMondial, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelPopMondial, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTabbedPaneSimulationOptions, javax.swing.GroupLayout.PREFERRED_SIZE, 553, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedMainPane.addTab("Simulation", jPanelSimulation);

        jPanelLegend.setBackground(new java.awt.Color(153, 153, 153));

        jLabel3.setText("Terrestre");

        jLabel4.setText("Maritime");

        jLabel5.setText("Aerien");

        jLabel7.setBackground(new java.awt.Color(0, 255, 255));
        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 255, 255));
        jLabel7.setText("-----------------");

        jLabel8.setBackground(new java.awt.Color(51, 51, 255));
        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(51, 51, 255));
        jLabel8.setText("-----------------");

        jLabel9.setBackground(new java.awt.Color(255, 51, 255));
        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 51, 255));
        jLabel9.setText("-----------------");

        javax.swing.GroupLayout jPanelLegendLayout = new javax.swing.GroupLayout(jPanelLegend);
        jPanelLegend.setLayout(jPanelLegendLayout);
        jPanelLegendLayout.setHorizontalGroup(
            jPanelLegendLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLegendLayout.createSequentialGroup()
                .addGroup(jPanelLegendLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanelLegendLayout.createSequentialGroup()
                        .addGroup(jPanelLegendLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanelLegendLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanelLegendLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(70, Short.MAX_VALUE))
        );
        jPanelLegendLayout.setVerticalGroup(
            jPanelLegendLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLegendLayout.createSequentialGroup()
                .addGroup(jPanelLegendLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanelLegendLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelLegendLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel9)))
        );

        jButtonScreenShotWorld.setText("Export vue monde");
        jButtonScreenShotWorld.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScreenShotWorldActionPerformed(evt);
            }
        });

        jLabelUndoRedoSliderText.setText("0");

        jSliderUndoRedo.setMaximum(0);
        jSliderUndoRedo.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSliderUndoRedoStateChanged(evt);
            }
        });

        jButtonChangeBackgroundImage.setText("Importer Image");
        jButtonChangeBackgroundImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonChangeBackgroundImageActionPerformed(evt);
            }
        });

        jMenu1.setText("File");

        jMenuItemNew.setText("Nouveau projet");
        jMenuItemNew.setToolTipText("");
        jMenuItemNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemNewActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItemNew);

        jMenuItemSave.setText("Sauvegarder projet");
        jMenuItemSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSaveActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItemSave);

        jMenuItemLoad.setText("Ouvrir un projet");
        jMenuItemLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemLoadActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItemLoad);

        jMainMenuBar.add(jMenu1);

        jMenu2.setText("Edit");

        jMenuItemUndo.setText("Undo");
        jMenuItemUndo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemUndoActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItemUndo);

        jMenuItemRedo.setText("Redo");
        jMenu2.add(jMenuItemRedo);

        jMainMenuBar.add(jMenu2);

        setJMenuBar(jMainMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPaneMap)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jSliderUndoRedo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelUndoRedoSliderText)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTabbedMainPane, javax.swing.GroupLayout.PREFERRED_SIZE, 384, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabelTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 453, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonChangeBackgroundImage, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButtonScreenShotWorld, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanelLegend, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButtonChangeBackgroundImage)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonScreenShotWorld))
                    .addComponent(jLabelTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanelLegend, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedMainPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPaneMap, javax.swing.GroupLayout.PREFERRED_SIZE, 709, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabelUndoRedoSliderText))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                                .addComponent(jSliderUndoRedo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItemNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemNewActionPerformed
        try {
            worldController.newProjet();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_jMenuItemNewActionPerformed

    private void jMenuItemSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSaveActionPerformed
        int returnValue = fileChooser.showSaveDialog(this);
        
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            worldController.save(fileChooser.getSelectedFile());
        }
    }//GEN-LAST:event_jMenuItemSaveActionPerformed

    private void jMenuItemLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemLoadActionPerformed
        int returnValue = fileChooser.showOpenDialog(this);
        
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            worldController.load(fileChooser.getSelectedFile());
        }
        
        UpdateJLinkList();
        
    }//GEN-LAST:event_jMenuItemLoadActionPerformed

    private void jMenuItemUndoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemUndoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItemUndoActionPerformed
    
    private void UnselectAddRegion() {
        mode = Mode.Idle;
        mousePoints.clear();
    }
    
    private void jPanelDrawMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelDrawMousePressed
        Point mousePoint = evt.getPoint();
        
        if (jTabbedMainPane.getSelectedIndex() == 1) {
            
            CountryDTO select = Utility.SelectCountry(worldController.GetCountries(), mousePoint);
            if(select != null) {
                SetSelectedCountry(select);
            }
            
            buttonGroupConception.clearSelection();
            mode = Mode.Idle;
        }
        
        switch (mode) {
            case AddRegion:
                CountryDTO select = Utility.SelectCountry(worldController.GetCountries(), mousePoint);
                if(select != null) {
                    if(mousePoints.isEmpty() || select.Id == countrySelected.Id) {
                        SetSelectedCountry(select);
                        mousePoints.add(mousePoint);
                        
                        if(mousePoints.size() >= 3) {
                            if(Utility.Distance(mousePoints.get(0), mousePoint) < 10) {                           
                                String name = jTextFieldRegionName.getText();
                                if(!Utility.StringIsNullOrEmpty(name)) {
                                    worldController.AddRegion(select.Id, mousePoints, name);
                                    UpdateJRegionList(select.Id);

                                    jTextFieldPercentageAddRegion.setBackground(Color.white);
                                    jTextFieldRegionName.setBackground(Color.white);

                                    UnselectAddRegion();
                                    mode = Mode.ModifyCountry;
                                    drawingPanel.repaint();
                                } else {
                                    if(Utility.StringIsNullOrEmpty(name)) jTextFieldRegionName.setBackground(Color.red);
                                }
                            }
                        }
                    } else {
                        UnselectAddRegion();
                        mode = Mode.ModifyCountry;
                    }
                } else {
                    UnselectAddRegion();
                    mode = Mode.ModifyCountry;
                }
                
                break;
            
            case AddCountry:
                mousePoints.add(mousePoint);
            
                if(mousePoints.size() == 2) {
                    try {
                        String name = jTextFieldCountryName.getText();
                        int population = Integer.parseInt(jTextFieldCountryPop.getText());
                        if(!Utility.StringIsNullOrEmpty(name) && population > 0) {
                            worldController.AddCountry(mousePoints, name, population);

                            jTextFieldCountryName.setBackground(Color.white);
                            jTextFieldCountryPop.setBackground(Color.white);
                             
                            drawingPanel.repaint();
                            mousePoints.clear();
                        } else {
                            if(Utility.StringIsNullOrEmpty(name)) jTextFieldCountryName.setBackground(Color.red);
                            if(population <= 0) jTextFieldCountryPop.setBackground(Color.red);
                        }
                    }catch(NumberFormatException e) {
                        jTextFieldCountryPop.setBackground(Color.red);
                    }
                    
                    mode = Mode.ModifyCountry;
                    mousePoints.clear();
                }
                break;
            case AddCountryIrregular:                
                mousePoints.add(mousePoint);
                if (mousePoints != null && !mousePoints.isEmpty() && mousePoints.size() >= 3) {
                    Point firstPoint = mousePoints.get(0);
                    Point lastPoint = mousePoints.get(mousePoints.size()-1);
                    if(Utility.Distance(firstPoint, lastPoint) < 10){
                        int indexLastPoint = mousePoints.size()-1;
                        mousePoints.remove(indexLastPoint);
                        try {
                            String name = jTextFieldCountryName.getText();
                            int population = Integer.parseInt(jTextFieldCountryPop.getText());
                            if(!Utility.StringIsNullOrEmpty(name) && population > 0) {
                                worldController.AddCountry(mousePoints, name, population);
                                
                                jTextFieldCountryName.setBackground(Color.white);
                                jTextFieldCountryPop.setBackground(Color.white);

                                drawingPanel.repaint();
                                mousePoints.clear();
                            } else {
                                if(Utility.StringIsNullOrEmpty(name)) jTextFieldCountryName.setBackground(Color.red);
                                if(population <= 0) jTextFieldCountryPop.setBackground(Color.red);
                            }
                        }catch(NumberFormatException e) {
                            jTextFieldCountryPop.setBackground(Color.red);
                        }

                        mode = Mode.ModifyCountry;
                        mousePoints.clear();
                    }                   
                }
                break;
            case ModifyCountry:
                SelectCountry(mousePoint);
                if(countrySelected != null) {
                    jTextFieldCountryName.setText(countrySelected.Name);
                    jTextFieldCountryPop.setText(Integer.toString(countrySelected.populationDTO.totalPopulationDTO));
                    UpdateJRegionList(countrySelected.Id);
                }
                break;
            case AddLink:
                List<CountryDTO> countries = worldController.GetCountries();
                CountryDTO selected = Utility.SelectCountry(countries, mousePoint);
                boolean found = false;
                if(selected != null && countrySelected != null) {
                    int linkIndex = jComboBoxAddLink.getSelectedIndex();
                    
                    double transmissionRate;
                    if (linkIndex == 0) {
                        transmissionRate = Double.parseDouble(jTextFieldBorderTransRate.getText());
                    } else if (linkIndex == 1) {
                        transmissionRate = Double.parseDouble(jTextFieldPortTransRate.getText());
                    } else {
                        transmissionRate = Double.parseDouble(jTextFieldAirportTransRate.getText());
                    }
                    
                    worldController.AddLink(countrySelected.Id, selected.Id, Link.LinkType.values()[linkIndex], transmissionRate);
          
                    SetSelectedCountry(null);
                    mode = Mode.Idle;
                } else {
                    SetSelectedCountry(selected);
                    found = true;
                }

                if(!found) {
                    SetSelectedCountry(null);
                }
            break;
                
        }
        
    }//GEN-LAST:event_jPanelDrawMousePressed

    private void jPanelDrawMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelDrawMouseDragged
        if (mode == Mode.ModifyCountry) {
            //CountryDTO country = Utility.SelectCountry(worldController.GetCountries(), evt.getPoint());
            if(countrySelected != null) {
                Point mousePoint = evt.getPoint();
                
                List<Point> pts = countrySelected.Shape.GetPoints();
                if(countrySelected.Shape instanceof IrregularForm) {
                    for(int i = 0 ; i < pts.size(); i++) {
                        if(Utility.Distance(pts.get(i), mousePoint) < 40) {
                            countrySelectedPointIndex = i;
                            break;
                        }
                    }
                } else {
                    if(Utility.Distance(pts.get(0), mousePoint) < 40) {
                        countrySelectedPointIndex = 0;
                    } else if(Utility.Distance(pts.get(2), mousePoint) < 40) {
                        countrySelectedPointIndex = 2;
                    }
                }
                
                if(countrySelectedPointIndex != -1) {
                    countrySelected.Shape.SetPointPosition(countrySelectedPointIndex, evt.getPoint());
                } else {
                    countrySelected.SetPosition(evt.getPoint());
                }
                
                worldController.UpdateCountry(countrySelected);
                drawingPanel.revalidate();
                drawingPanel.repaint();
            }
        }
    }//GEN-LAST:event_jPanelDrawMouseDragged

    private void jPanelDrawMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelDrawMouseMoved
        CountryDTO country = Utility.SelectCountry(worldController.GetCountries(), evt.getPoint());
        boolean found = false;
        
        if(country != null) {
            onHoverCountry = country;
            onHoverMousePosition = evt.getPoint();
            found = true;
        }

        if(!found) {
            onHoverCountry = null;
        }

        drawingPanel.repaint();
    }//GEN-LAST:event_jPanelDrawMouseMoved

    private void jPanelDrawMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_jPanelDrawMouseWheelMoved
        drawingPanel.Zoom(evt);
    }//GEN-LAST:event_jPanelDrawMouseWheelMoved

    private void jButtonAddMesureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddMesureActionPerformed
        if(countrySelected != null) {
            try {
                String mesureName = jTextFieldMesureName.getText();
                double threshold = Double.parseDouble(jTextFieldThreshold.getText())/100;
                double adhesion = Double.parseDouble(jTextFieldAdhesionRate.getText())/100;
                double effectTransmissionRate = Double.parseDouble(jTextFieldEffectTransmission.getText())/100;
                double effectReproductionRate = Double.parseDouble(jTextFieldEffectReproductionRate.getText());
                if(!Utility.StringIsNullOrEmpty(mesureName) && adhesion >= 0 && threshold >=0 && 
                        effectTransmissionRate >=0 && effectReproductionRate >=0) 
                { 
                    worldController.AddMesure(countrySelected.Id, adhesion, jCheckBoxActiveMesure.isSelected(), mesureName, threshold,
                                              effectTransmissionRate, effectReproductionRate);
                    UpdateJMesureList(countrySelected.Id);

                    jTextFieldMesureName.setBackground(Color.white);
                    jTextFieldAdhesionRate.setBackground(Color.white);
                    drawingPanel.repaint();
                } else {
                    if (Utility.StringIsNullOrEmpty(mesureName)) jTextFieldMesureName.setBackground(Color.red);
                    if (adhesion < 0) jTextFieldAdhesionRate.setBackground(Color.red);
                    if (threshold < 0) jTextFieldThreshold.setBackground(Color.red);
                    if (effectTransmissionRate < 0) jTextFieldEffectTransmission.setBackground(Color.red);
                    if (effectReproductionRate < 0) jTextFieldEffectReproductionRate.setBackground(Color.red);
                }
            } catch(NumberFormatException e) {
                jTextFieldAdhesionRate.setBackground(Color.red);
            }
        }
    }//GEN-LAST:event_jButtonAddMesureActionPerformed

    private void jButtonForwardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonForwardActionPerformed
        worldController.Redo();
        UpdateSliderUndoRedo();
    }//GEN-LAST:event_jButtonForwardActionPerformed

    private void jButtonBacktrackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBacktrackActionPerformed
        worldController.Undo();
        UpdateSliderUndoRedo();
    }//GEN-LAST:event_jButtonBacktrackActionPerformed

    private void jBtnPlayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnPlayActionPerformed
        try {
            int timeInMs = (int)(Double.parseDouble(jTextFieldSimulationTimeStep.getText()) * 1000);
            worldController.StartSimulation(timeInMs);
            jTextFieldSimulationTimeStep.setBackground(Color.white);
        } catch(NumberFormatException e) {
            jTextFieldSimulationTimeStep.setBackground(Color.red);
        } catch(NotAllPopulationAssign e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }//GEN-LAST:event_jBtnPlayActionPerformed

    private void jBtnPauseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnPauseActionPerformed
        worldController.pauseSimulation();
        jSliderUndoRedo.setEnabled(true);
    }//GEN-LAST:event_jBtnPauseActionPerformed

    private void jBtnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnResetActionPerformed
        worldController.resetSimulation();
        jSliderUndoRedo.setEnabled(true);
    }//GEN-LAST:event_jBtnResetActionPerformed

    private void jBtnChangeSimulationTimeStepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnChangeSimulationTimeStepActionPerformed
        try {
            if(worldController.IsRunning()) {
                int timeInMs = (int)(Double.parseDouble(jTextFieldSimulationTimeStep.getText()) * 1000);
                worldController.pauseSimulation();
                worldController.StartSimulation(timeInMs);
                
                jTextFieldSimulationTimeStep.setBackground(Color.white);
            }
        } catch(NumberFormatException e) {
            jTextFieldSimulationTimeStep.setBackground(Color.red);
        } catch(NotAllPopulationAssign e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }//GEN-LAST:event_jBtnChangeSimulationTimeStepActionPerformed

    private void jButtonDeleteMesureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteMesureActionPerformed
        int index = jListMesures.getSelectedIndex();
        if(index != -1) {
            UUID mesureId = worldController.GetHealthMesures(countrySelected.Id).get(index).Id;
            worldController.RemoveMesure(countrySelected.Id, mesureId);
            UpdateJMesureList(countrySelected.Id);
        }
    }//GEN-LAST:event_jButtonDeleteMesureActionPerformed

    private void jButtonDeleteLinkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteLinkActionPerformed
        List<LinkDTO> links = worldController.GetLinks();
        int[] index = jListLinks.getSelectedIndices();

        for(int i = 0; i < index.length; i++) {
            UUID id = links.get(index[i]).Id;
            worldController.RemoveLink(id);
        }
    }//GEN-LAST:event_jButtonDeleteLinkActionPerformed

    private void jListLinksValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListLinksValueChanged
        List<LinkDTO> links = worldController.GetLinks();
        links.stream().filter(l -> (l.IsSelected)).forEachOrdered(l -> {
            worldController.UpdateSelectionStateLink(l.Id, false);
            
            jTextFieldLinkTransRate.setText(Double.toString(l.TransmissionRate));
        });

        int[] index = jListLinks.getSelectedIndices();
        for(int i = 0; i < index.length; i++) {
            UUID id = links.get(index[i]).Id;
            
            worldController.UpdateSelectionStateLink(id, true);
            
        }

        drawingPanel.repaint();
    }//GEN-LAST:event_jListLinksValueChanged

    private void jButtonModifyRegionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModifyRegionActionPerformed
        int index = jListRegionsList.getSelectedIndex();
        if(countrySelected != null) {
            if(index != -1) {
                RegionDTO region = countrySelected.Regions.get(index);

                try {
                    double percentage = Double.parseDouble(jTextFieldPercentageAddRegion.getText()); //TODO: > 0 && < 100
                    region.PercentagePop = percentage / 100.0;

                    worldController.UpdateRegion(countrySelected.Id, region);
                    UpdateJRegionList(countrySelected.Id);

                    jTextFieldPercentageAddRegion.setBackground(Color.white);
                } catch(NumberFormatException e) {
                    jTextFieldPercentageAddRegion.setBackground(Color.red);
                }
            }
        }
    }//GEN-LAST:event_jButtonModifyRegionActionPerformed

    private void jButtonAddRegionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddRegionActionPerformed
        if(countrySelected != null) {
            UnselectAddRegion();
            mode = Mode.AddRegion;
        }
    }//GEN-LAST:event_jButtonAddRegionActionPerformed

    private void jButtonRemoveRegionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRemoveRegionActionPerformed
        if(countrySelected != null) {
            int index = jListRegionsList.getSelectedIndex();
            if(index != -1) {
                if(countrySelected.Regions.size() > 1) {
                    UUID regionId = countrySelected.Regions.get(index).Id;
                    worldController.RemoveRegion(countrySelected.Id, regionId);
                    UpdateJRegionList(countrySelected.Id);
                }
            }
        }
    }//GEN-LAST:event_jButtonRemoveRegionActionPerformed

    private void jListRegionsListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListRegionsListValueChanged
        if(countrySelected != null) {
            countrySelected = worldController.GetCountries().stream().filter(c -> c.Id.equals(countrySelected.Id)).findFirst().get();
            List<RegionDTO> regions = countrySelected.Regions;

            regions.stream().filter(r -> (r.IsSelected)).forEachOrdered(r -> {
                worldController.UpdateSelectionStateRegion(countrySelected.Id, r.Id, false);
            });

            int index = jListRegionsList.getSelectedIndex();
            if(index != -1) {
                worldController.UpdateSelectionStateRegion(countrySelected.Id, regions.get(index).Id, true);
                double percentage = worldController.GetCountries().stream().filter(c -> c.Id.equals(countrySelected.Id)).findFirst().get().Regions.get(index).PercentagePop;
                jTextFieldPercentageAddRegion.setText(String.valueOf(percentage * 100.0));
            }
        }
    }//GEN-LAST:event_jListRegionsListValueChanged

    private void jTextFieldCountryPopKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCountryPopKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if(countrySelected != null) {
                int totalPopulation = Integer.parseInt(jTextFieldCountryPop.getText());
                worldController.UpdateTotalPopulation(countrySelected.Id, totalPopulation);
                UpdateJRegionList(countrySelected.Id);
            }
        }
    }//GEN-LAST:event_jTextFieldCountryPopKeyPressed

    private void jTextFieldCountryNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCountryNameKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            countrySelected.Name = jTextFieldCountryName.getText();
            worldController.UpdateCountry(countrySelected);
            drawingPanel.revalidate();
            drawingPanel.repaint();
        }
    }//GEN-LAST:event_jTextFieldCountryNameKeyPressed

    private void jButtonDeleteCountryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteCountryActionPerformed
        if(countrySelected != null) {
            worldController.RemoveCountry(countrySelected.Id);
        }
    }//GEN-LAST:event_jButtonDeleteCountryActionPerformed

    private void jToggleBtnAddLinkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleBtnAddLinkActionPerformed
        if (jToggleBtnAddLink.isSelected()){
            mode = Mode.AddLink;
            SetSelectedCountry(null);
            jPanelCountry.setVisible(false);
            jPanelLink.setVisible(true);
            jPanelBlank.setVisible(false);
        }
    }//GEN-LAST:event_jToggleBtnAddLinkActionPerformed

    private void jToggleBtnModifyCountryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleBtnModifyCountryActionPerformed
        // TODO add your handling code here:
        if (jToggleBtnModifyCountry.isSelected()){
            mode = Mode.ModifyCountry;
            SetSelectedCountry(null);
            jPanelCountry.setVisible(true);
            jPanelLink.setVisible(false);
            jPanelBlank.setVisible(false);
        }
    }//GEN-LAST:event_jToggleBtnModifyCountryActionPerformed

    private void jButtonResetZoomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonResetZoomActionPerformed
        drawingPanel.ResetZoom();
    }//GEN-LAST:event_jButtonResetZoomActionPerformed

    private void jButtonScreenShotWorldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScreenShotWorldActionPerformed
        int returnValue = fileChooser.showSaveDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            drawingPanel.SaveScreenShot(fileChooser.getSelectedFile());
        }
       
    }//GEN-LAST:event_jButtonScreenShotWorldActionPerformed

    private void jSliderUndoRedoStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSliderUndoRedoStateChanged
        JSlider slider = (JSlider) evt.getSource();
        if (slider.getValueIsAdjusting()) {
          int value = slider.getValue();
          worldController.UndoRedoAt(value);
          UpdateSliderUndoRedo();
        }
    }//GEN-LAST:event_jSliderUndoRedoStateChanged

    private void jButtonCreateRegularCountryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCreateRegularCountryActionPerformed
        SetSelectedCountry(null);
        mode = Mode.AddCountry; //TODO: Sans doute faire un mode = Mode.AddCountryRregular
    }//GEN-LAST:event_jButtonCreateRegularCountryActionPerformed

    private void jButtonCreateIrregularCountryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCreateIrregularCountryActionPerformed
        SetSelectedCountry(null);
        mode = Mode.AddCountryIrregular; 
    }//GEN-LAST:event_jButtonCreateIrregularCountryActionPerformed

    private void jButtonApplyAllLinksTravelRateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonApplyAllLinksTravelRateActionPerformed
        // TODO add your handling code here:
        worldController.setAllLinksTransmissionRate(Double.parseDouble(jTextFieldBorderTransRate.getText())/100, 
                Double.parseDouble(jTextFieldPortTransRate.getText())/100, Double.parseDouble(jTextFieldAirportTransRate.getText())/100,
                Double.parseDouble(jTextFieldRegionLinkTrans.getText())/100);
        UpdateJLinkList();
    }//GEN-LAST:event_jButtonApplyAllLinksTravelRateActionPerformed

    private void jButtonChangeBackgroundImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonChangeBackgroundImageActionPerformed
        int returnValue = imageChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            drawingPanel.loadImageBackground(imageChooser.getSelectedFile());
        }
    }//GEN-LAST:event_jButtonChangeBackgroundImageActionPerformed


    private void jButtonCreateGraphicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCreateGraphicActionPerformed
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(deathsNum);
        dataset.addSeries(infectedNum);
        dataset.addSeries(nonInfectedNum);

        JFreeChart chart = ChartFactory.createXYLineChart("Statistiques de la pandémie", "jours", "nombre", dataset);
        chart.setBackgroundPaint(Color.GRAY);
        chart.getTitle().setPaint(Color.RED);
        ChartFrame frame = new ChartFrame("Statistiques de la pandémie", chart);
        frame.setVisible(true);
        frame.setSize(450, 350);
    }//GEN-LAST:event_jButtonCreateGraphicActionPerformed

    private void jCheckBoxActiveMesureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxActiveMesureActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jCheckBoxActiveMesureActionPerformed

    private void jListLinksSimValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListLinksSimValueChanged
        // TODO add your handling code here:
        List<LinkDTO> links = worldController.GetLinks();
        links.stream().filter(l -> (l.IsSelected)).forEachOrdered(l -> {
            worldController.UpdateSelectionStateLink(l.Id, false);
            
        });

        int[] index = jListLinksSim.getSelectedIndices();
        for(int i = 0; i < index.length; i++) {
            UUID id = links.get(index[i]).Id;
            
            worldController.UpdateSelectionStateLink(id, true);
        }

        drawingPanel.repaint();
    }//GEN-LAST:event_jListLinksSimValueChanged

    private void jButtonAddCloseLinkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddCloseLinkActionPerformed
        // TODO add your handling code here:
        List<LinkDTO> links = worldController.GetLinks();
        //List<CloseLinkDTO> closedLinks = worldController.getClosedLinks();
        
        int[] index = jListLinksSim.getSelectedIndices();

        for(int i = 0; i < index.length; i++) {
            UUID id = links.get(index[i]).Id;
            worldController.addCloseLink(id, Double.parseDouble(jTextFieldCloseLinkAR.getText())/100, 
                                         Double.parseDouble(jTextFieldCloseLinkThreshold.getText())/100);
            UpdateJListClosedLinks();
        }
    }//GEN-LAST:event_jButtonAddCloseLinkActionPerformed

    private void jButtonDeleteCloseLinkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteCloseLinkActionPerformed
        //à débugger
        //int index = jListClosedLinks.getSelectedIndex();
        //if(index != -1) {
           // UUID closeLinkId = worldController.getClosedLinks().get(index).ConcernedLink;
           // worldController.removeCloseLink(closeLinkId);   
        //}
       
        //UpdateJListClosedLinks();
        
    }//GEN-LAST:event_jButtonDeleteCloseLinkActionPerformed

    private void jComboBoxCountryPatientZeroPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jComboBoxCountryPatientZeroPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxCountryPatientZeroPropertyChange

    private void jComboBoxCountryPatientZeroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxCountryPatientZeroActionPerformed
        // TODO add your handling code here:
        int index = jComboBoxCountryPatientZero.getSelectedIndex();
        if(index !=-1){
            worldController.setCurrentCountryPatientZeroIndex(index);
            //updateCountryListUI();
        }
    }//GEN-LAST:event_jComboBoxCountryPatientZeroActionPerformed

    private void jButtonDeleteDiseaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteDiseaseActionPerformed
        int index = jComboBoxDiseases.getSelectedIndex();
        if(index != -1) {
            worldController.RemoveDisease(index);
            worldController.AddUndoRedo();
            updateDiseasesUI();
        }  
    }//GEN-LAST:event_jButtonDeleteDiseaseActionPerformed

    private void jButtonSaveNewDiseaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveNewDiseaseActionPerformed
        // TODO add your handling code here

        try {
            double infectionRate = Double.parseDouble(jTextFieldReproductionRate.getText())/100;
            double mortalityRate = Double.parseDouble(jTextFieldMortalityRate.getText())/100;
            double cureRate = Double.parseDouble(jTextFieldCuredRate.getText())/100;
            boolean found = false;
            UUID id = UUID.randomUUID();
            String diseaseName = (jTextFieldDiseaseName.getText());

            if(diseaseName.length()>0 && infectionRate >= 0 && infectionRate <=1 && mortalityRate >=0 && mortalityRate <=1 &&
                cureRate >= 0 && cureRate <=1) {
                for(Disease n : worldController.getDiseaseList()){
                    if(n.getName().equals(diseaseName)){
                        found = true;
                        id = n.getId();
                        break;
                    }
                }
                
                if(found){
                    worldController.UpdateDisease(id, diseaseName, infectionRate, mortalityRate, cureRate);
                }else{
                    worldController.createDisease(diseaseName, infectionRate, mortalityRate, cureRate);
                    updateDiseasesUI();
                }
                
                worldController.AddUndoRedo();
                UpdateSliderUndoRedo();

                jTextFieldReproductionRate.setBackground(Color.white);
                jTextFieldMortalityRate.setBackground(Color.white);
                jTextFieldCuredRate.setBackground(Color.white); 
            }else{
                if(infectionRate < 0) jTextFieldReproductionRate.setBackground(Color.red);
                if(mortalityRate < 0) jTextFieldMortalityRate.setBackground(Color.red);
                if(cureRate < 0) jTextFieldCuredRate.setBackground(Color.red);
            }
        } catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Un des champs n'est pas valide");
        }
    }//GEN-LAST:event_jButtonSaveNewDiseaseActionPerformed

    private void jComboBoxDiseasesPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jComboBoxDiseasesPropertyChange
        //        int index = jComboBoxDiseases.getSelectedIndex();
        //        if(index !=-1){
            //            worldController.setCurrentDiseaseIndex(index);
            //            updateDiseasesUI();
            //        }
    }//GEN-LAST:event_jComboBoxDiseasesPropertyChange

    private void jComboBoxDiseasesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxDiseasesActionPerformed
        // TODO add your handling code here:
        int index = jComboBoxDiseases.getSelectedIndex();
        if(index !=-1){
            worldController.setCurrentDiseaseIndex(index);
            updateDiseasesUI();
        }
    }//GEN-LAST:event_jComboBoxDiseasesActionPerformed

    private void jButtonApplyDiseaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonApplyDiseaseActionPerformed

        //        try {
            //            double infectionRate = Double.parseDouble(jTextFieldReproductionRate.getText())/100;
            //            double mortalityRate = Double.parseDouble(jTextFieldMortalityRate.getText())/100;
            //            double cureRate = Double.parseDouble(jTextFieldCuredRate.getText())/100;
            //            if(infectionRate >= 0 && infectionRate <=1 && mortalityRate >=0 && mortalityRate <=1 &&
                //                cureRate >= 0 && cureRate <=1) {
                //                    worldController.UpdateDiseaseFromDTO(infectionRate, mortalityRate, cureRate);
                ////                    DiseaseDTO disease = new DiseaseDTO()
                ////                    worldController.getSimulation().UpdateDiseaseFromDTO(infectionRate, mortalityRate, cureRate);
                //                    jTextFieldReproductionRate.setBackground(Color.white);
                //                    jTextFieldMortalityRate.setBackground(Color.white);
                //                    jTextFieldCuredRate.setBackground(Color.white);
                //                }
            //            else{
                //                if(infectionRate < 0) jTextFieldReproductionRate.setBackground(Color.red);
                //                if(mortalityRate < 0) jTextFieldMortalityRate.setBackground(Color.red);
                //                if(cureRate < 0) jTextFieldCuredRate.setBackground(Color.red);
                //            }
            //        } catch(NumberFormatException e) {
            //            JOptionPane.showMessageDialog(this, "Un des champs n'est pas valide");
            //        }
    }//GEN-LAST:event_jButtonApplyDiseaseActionPerformed

    private void jTextFieldMortalityRateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldMortalityRateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldMortalityRateActionPerformed

    private void jTextFieldNumberOfZeroPatientsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldNumberOfZeroPatientsActionPerformed
        // TODO add your handling code here:   
        
    }//GEN-LAST:event_jTextFieldNumberOfZeroPatientsActionPerformed

    private void jButtonApplyPatientZeroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonApplyPatientZeroActionPerformed
        // TODO add your handling code here:
        try{
            int nbOfPatient = (int)Double.parseDouble(jTextFieldReproductionRate.getText());
            if(nbOfPatient > 0) {
                worldController.setNbOfPatientZero(nbOfPatient);
            }  
        }
        catch(NumberFormatException nfe){
            
        }
    }//GEN-LAST:event_jButtonApplyPatientZeroActionPerformed

    private void jTextFieldLinkTransRateKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLinkTransRateKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            List <LinkDTO> listLinks = worldController.GetLinks();
        
            int index = jListLinks.getSelectedIndex();
        
            UUID id = listLinks.get(index).Id;
            double transmissionRate = Double.parseDouble(jTextFieldLinkTransRate.getText());
            
            worldController.setLinkTransmissionRate(id, transmissionRate);
            UpdateJLinkList();
        }
        
    }//GEN-LAST:event_jTextFieldLinkTransRateKeyPressed


    public void Draw(Graphics2D g2d){
        worldController.Draw(g2d, mousePoints); 
        if(onHoverCountry != null) {
            worldController.DrawCountryInfo(g2d, onHoverMousePosition, onHoverCountry);
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new MainWindow().setVisible(true);
                } catch (CloneNotSupportedException ex) {
                    Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroupConception;
    private javax.swing.ButtonGroup buttonGroupSimulation;
    private javax.swing.JButton jBtnChangeSimulationTimeStep;
    private javax.swing.JButton jBtnPause;
    private javax.swing.JButton jBtnPlay;
    private javax.swing.JButton jBtnReset;
    private javax.swing.JButton jButtonAddCloseLink;
    private javax.swing.JButton jButtonAddMesure;
    private javax.swing.JButton jButtonAddRegion;
    private javax.swing.JButton jButtonApplyAllLinksTravelRate;
    private javax.swing.JButton jButtonApplyDisease;
    private javax.swing.JButton jButtonApplyPatientZero;
    private javax.swing.JButton jButtonBacktrack;
    private javax.swing.JButton jButtonChangeBackgroundImage;
    private javax.swing.JButton jButtonCreateGraphic;
    private javax.swing.JButton jButtonCreateIrregularCountry;
    private javax.swing.JButton jButtonCreateRegularCountry;
    private javax.swing.JButton jButtonDeleteCloseLink;
    private javax.swing.JButton jButtonDeleteCountry;
    private javax.swing.JButton jButtonDeleteDisease;
    private javax.swing.JButton jButtonDeleteLink;
    private javax.swing.JButton jButtonDeleteMesure;
    private javax.swing.JButton jButtonForward;
    private javax.swing.JButton jButtonModifyCloseLink;
    private javax.swing.JButton jButtonModifyRegion;
    private javax.swing.JButton jButtonRemoveRegion;
    private javax.swing.JButton jButtonResetZoom;
    private javax.swing.JButton jButtonSaveNewDisease;
    private javax.swing.JButton jButtonScreenShotWorld;
    private javax.swing.JCheckBox jCheckBoxActiveMesure;
    private javax.swing.JComboBox<String> jComboBoxAddLink;
    private javax.swing.JComboBox<String> jComboBoxCountryPatientZero;
    private javax.swing.JComboBox<String> jComboBoxDiseases;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelAdhesionRate;
    private javax.swing.JLabel jLabelCase;
    private javax.swing.JLabel jLabelCloseLinkAR;
    private javax.swing.JLabel jLabelCured;
    private javax.swing.JLabel jLabelDayElapsed;
    private javax.swing.JLabel jLabelDead;
    private javax.swing.JLabel jLabelDiseaseReminderName;
    private javax.swing.JLabel jLabelEffectReproductionRate;
    private javax.swing.JLabel jLabelEffetTransmission;
    private javax.swing.JLabel jLabelLinksSimTitle;
    private javax.swing.JLabel jLabelMaxRRate;
    private javax.swing.JLabel jLabelMesureName;
    private javax.swing.JLabel jLabelModCountryName;
    private javax.swing.JLabel jLabelModCountryPop;
    private javax.swing.JLabel jLabelModifyLinkTrans;
    private javax.swing.JLabel jLabelPopMondial;
    private javax.swing.JLabel jLabelR;
    private javax.swing.JLabel jLabelRegionLink;
    private javax.swing.JLabel jLabelReproductionRate;
    private javax.swing.JLabel jLabelSelectionCountryPatientZero;
    private javax.swing.JLabel jLabelThreshold;
    private javax.swing.JLabel jLabelTimeLapse;
    private javax.swing.JLabel jLabelTitle;
    private javax.swing.JLabel jLabelTitleCase;
    private javax.swing.JLabel jLabelTitleCreationEdtionPays;
    private javax.swing.JLabel jLabelTitleCured;
    private javax.swing.JLabel jLabelTitleCuredRate;
    private javax.swing.JLabel jLabelTitleDayElapsed;
    private javax.swing.JLabel jLabelTitleDead;
    private javax.swing.JLabel jLabelTitleDeseaseName;
    private javax.swing.JLabel jLabelTitleEditionRegion;
    private javax.swing.JLabel jLabelTitleMortalityRate;
    private javax.swing.JLabel jLabelTitleNumberOfZeroPatients;
    private javax.swing.JLabel jLabelTitlePopMondial;
    private javax.swing.JLabel jLabelTitleRegionList;
    private javax.swing.JLabel jLabelTitleRegionName;
    private javax.swing.JLabel jLabelTitleReproductionRate;
    private javax.swing.JLabel jLabelUndoRedoSliderText;
    private javax.swing.JList<String> jListClosedLinks;
    private javax.swing.JList<String> jListLinks;
    private javax.swing.JList<String> jListLinksSim;
    private javax.swing.JList<String> jListMesures;
    private javax.swing.JList<String> jListRegionsList;
    private javax.swing.JMenuBar jMainMenuBar;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuItem jMenuItemLoad;
    private javax.swing.JMenuItem jMenuItemNew;
    private javax.swing.JMenuItem jMenuItemRedo;
    private javax.swing.JMenuItem jMenuItemSave;
    private javax.swing.JMenuItem jMenuItemUndo;
    private javax.swing.JPanel jPanelBlank;
    private javax.swing.JPanel jPanelConception;
    private javax.swing.JPanel jPanelConceptionOptions;
    private javax.swing.JPanel jPanelCountry;
    private javax.swing.JPanel jPanelCuredRate;
    private javax.swing.JPanel jPanelDeseaseParams;
    private javax.swing.JPanel jPanelDraw;
    private javax.swing.JPanel jPanelFermetureLiens;
    private javax.swing.JPanel jPanelHealthMesures;
    private javax.swing.JPanel jPanelLegend;
    private javax.swing.JPanel jPanelLink;
    private javax.swing.JPanel jPanelMortalityRate;
    private javax.swing.JPanel jPanelNumberOfZeroPatients;
    private javax.swing.JPanel jPanelRegionOptions;
    private javax.swing.JPanel jPanelReproductionRate;
    private javax.swing.JPanel jPanelSimulation;
    private javax.swing.JPanel jPanelStatistics;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPaneClosedLinks;
    private javax.swing.JScrollPane jScrollPaneLinks;
    private javax.swing.JScrollPane jScrollPaneMap;
    private javax.swing.JScrollPane jScrollPaneOtherMeasures;
    private javax.swing.JScrollPane jScrollPaneRegionsList;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSlider jSliderUndoRedo;
    private javax.swing.JTabbedPane jTabbedMainPane;
    private javax.swing.JTabbedPane jTabbedPaneSimulationOptions;
    private javax.swing.JTextField jTextFieldAdhesionRate;
    private javax.swing.JTextField jTextFieldAirportTransRate;
    private javax.swing.JTextField jTextFieldBorderTransRate;
    private javax.swing.JTextField jTextFieldCloseLinkAR;
    private javax.swing.JTextField jTextFieldCloseLinkThreshold;
    private javax.swing.JTextField jTextFieldCountryName;
    private javax.swing.JTextField jTextFieldCountryPop;
    private javax.swing.JTextField jTextFieldCuredRate;
    private javax.swing.JTextField jTextFieldDiseaseName;
    private javax.swing.JTextField jTextFieldEffectReproductionRate;
    private javax.swing.JTextField jTextFieldEffectTransmission;
    private javax.swing.JTextField jTextFieldLinkTransRate;
    private javax.swing.JTextField jTextFieldMesureName;
    private javax.swing.JTextField jTextFieldMortalityRate;
    private javax.swing.JTextField jTextFieldNumberOfZeroPatients;
    private javax.swing.JTextField jTextFieldPercentageAddRegion;
    private javax.swing.JTextField jTextFieldPortTransRate;
    private javax.swing.JTextField jTextFieldRegionLinkTrans;
    private javax.swing.JTextField jTextFieldRegionName;
    private javax.swing.JTextField jTextFieldReproductionRate;
    private javax.swing.JTextField jTextFieldSimulationTimeStep;
    private javax.swing.JTextField jTextFieldThreshold;
    private javax.swing.JToggleButton jToggleBtnAddLink;
    private javax.swing.JToggleButton jToggleBtnModifyCountry;
    // End of variables declaration//GEN-END:variables
}
