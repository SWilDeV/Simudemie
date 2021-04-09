/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.ui;

import ca.ulaval.glo2004.domain.CountryDTO;
import ca.ulaval.glo2004.domain.DiseaseDTO;
import ca.ulaval.glo2004.domain.HealthMesureDTO;
import ca.ulaval.glo2004.domain.Link;
import ca.ulaval.glo2004.domain.LinkDTO;
import ca.ulaval.glo2004.domain.NotAllPopulationAssign;
import ca.ulaval.glo2004.domain.Population;
import ca.ulaval.glo2004.domain.RegionDTO;
import ca.ulaval.glo2004.domain.Utility;
import ca.ulaval.glo2004.domain.WorldController;
import ca.ulaval.glo2004.domain.WorldObserver;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author melanietremblay
 */
public class MainWindow extends javax.swing.JFrame implements WorldObserver {
    
    public DrawingPanel drawingPanel;
    public WorldController worldController = new WorldController();
    
    public List<Point> countryPts = new ArrayList<>();
    public List<Point> regionPts = new ArrayList<>();
    public CountryDTO countrySelected = null;

    public enum Mode {Idle, AddCountry, ModifyCountry, AddLink, ModifyLink, Select, AddRegion};
    public Mode mode = Mode.Idle;
    private int test = 0;
    private CountryDTO onHoverCountry = null; //Je sais que c'est pas bien, mais pour test, on va faire ca.
    private Point onHoverMousePosition = new Point(); //Je sais que c'est pas bien, mais pour test, on va faire ca.
    private final JFileChooser fileChooser;
 
    /**
     * Creates new form MainWindow
     */
    public MainWindow() {
        initComponents();
        
        drawingPanel = new DrawingPanel(this, jPanelDraw);
        jPanelDraw.add(drawingPanel);
        fileChooser = new JFileChooser();
        
        worldController.Subscribe(this);
    }
    
    @Override
    public void OnSimulationTick(int day, int deads, int infected, int PopTot) {
        System.err.println("Jour:" + day);
        UpdateSimulationUI();
    }
    
    @Override
    public void OnSimulationReset() {
        UpdateSimulationUI();
    }
    
    private void UpdateSimulationUI() {
        Population pop = worldController.getWorld().getWorldPopulation();
        jLabelDayElapsed.setText(String.valueOf(worldController.GetElapsedDay()));
        jLabelDead.setText(String.valueOf(pop.getDeadPopulation()));
        jLabelCase.setText(String.valueOf(pop.getInfectedPopulation()));
        jLabelCured.setText(String.valueOf(pop.getNonInfectedPopulation()));
        jLabelPopMondial.setText(String.valueOf(pop.getTotalPopulation()));
        drawingPanel.repaint();
    }
    
    @Override
    public void OnLinksUpdated() {
        UpdateJLinkList();
    }
    
    @Override
    public void OnCountryCreated(CountryDTO country) {
        UpdateJRegionList(country);
    }
    
    @Override
    public void OnSimulationStarted() {
        if(countrySelected != null){
            UpdateJRegionList(countrySelected.Id);
        }
    }
    
    @Override
    public void OnProjectLoaded() {
        UpdateSimulationUI();
        DiseaseDTO disease = worldController.GetDiseaseDTO();
        jTextFieldMortalityRate.setText(String.valueOf(disease.getMortalityRateDTO() * 100));
        jTextFieldReproductionRate.setText(String.valueOf(disease.getInfectionRateDTO() * 100));
        jTextFieldCuredRate.setText(String.valueOf(disease.getCureRateDTO() * 100));
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
            
            name += l.Country1.Name + " <-> " + l.Country2.Name;
            listModel.addElement(name);
        }
        jListLinks.setModel(listModel);
        drawingPanel.repaint();
    }
    
    private void UpdateJRegionList(UUID countryId) {
        CountryDTO country = worldController.GetCountries().stream().filter(c -> c.Id.equals(countryId)).findFirst().get();
        UpdateJRegionList(country);
    }
    
    private void UpdateJRegionList(CountryDTO country) {
        DefaultListModel listModel = new DefaultListModel();
        country.Regions.forEach(r -> {
            listModel.addElement(String.format("%s | Pourcentage: %s | Population: %s", r.Name, r.PercentagePop * 100, r.SubPopulation.totalPopulationDTO));
        });
        jListRegionsList.setModel(listModel);
        drawingPanel.repaint();
    }
    
    private void UpdateJMesureList(UUID countryId) {
        List<HealthMesureDTO> mesures = worldController.GetHealthMesures(countryId);
        if(mesures != null) { 
            DefaultListModel listMesuresModel = new DefaultListModel();

             mesures.forEach(m -> {
                 listMesuresModel.addElement(m.MesureName + " " + m.IsActive + " " + m.AdhesionRate + "%");
             });
             jListMesures.setModel(listMesuresModel);
             drawingPanel.repaint();
        }
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
        jToggleBtnAddCountry = new javax.swing.JToggleButton();
        jToggleBtnAddCountryIrregular = new javax.swing.JToggleButton();
        jToggleBtnModifyCountry = new javax.swing.JToggleButton();
        jToggleBtnAddLink = new javax.swing.JToggleButton();
        jToggleBtnModifyLink = new javax.swing.JToggleButton();
        jPanelConceptionOptions = new javax.swing.JPanel();
        jPanelBlank = new javax.swing.JPanel();
        jPanelAddCountry = new javax.swing.JPanel();
        jLabelAddCountryName = new javax.swing.JLabel();
        jLabelAddCountryPop = new javax.swing.JLabel();
        jTextFieldAddCountryName = new javax.swing.JTextField();
        jTextFieldAddCountryPop = new javax.swing.JTextField();
        jPanelModifyCountry = new javax.swing.JPanel();
        jButtonDeleteCountry = new javax.swing.JButton();
        jLabelModCountryName = new javax.swing.JLabel();
        jLabelModCountryPop = new javax.swing.JLabel();
        jTextFieldChangeCountryName = new javax.swing.JTextField();
        jTextFieldChangeCountryPop = new javax.swing.JTextField();
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
        jPanelAddLink = new javax.swing.JPanel();
        jComboBoxAddLink = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jScrollPaneLinks = new javax.swing.JScrollPane();
        jListLinks = new javax.swing.JList<>();
        jButtonDeleteLink = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jPanelModifyLink = new javax.swing.JPanel();
        jComboBoxModifyLink = new javax.swing.JComboBox<>();
        jLabelModifyLinkType = new javax.swing.JLabel();
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
        jPanelTransmissionRate = new javax.swing.JPanel();
        jButtonApplyDisease = new javax.swing.JButton();
        jPanelHealthMesures = new javax.swing.JPanel();
        jLabelMesureName = new javax.swing.JLabel();
        jTextFieldMesureName = new javax.swing.JTextField();
        jLabelAdhesionRate = new javax.swing.JLabel();
        jTextFieldAdhesionRate = new javax.swing.JTextField();
        jCheckBoxActiveMesure = new javax.swing.JCheckBox();
        jScrollPaneOtherMeasures = new javax.swing.JScrollPane();
        jListMesures = new javax.swing.JList<>();
        jButtonAddMesure = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jCheckBox2 = new javax.swing.JCheckBox();
        jButtonDeleteMesure = new javax.swing.JButton();
        jLabelTitlePopMondial = new javax.swing.JLabel();
        jLabelCured = new javax.swing.JLabel();
        jBtnChangeSimulationTimeStep = new javax.swing.JButton();
        jPanelLegend = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jButtonScreenShotWorld = new javax.swing.JButton();
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

        buttonGroupConception.add(jToggleBtnAddCountry);
        jToggleBtnAddCountry.setText("Ajouter pays");
        jToggleBtnAddCountry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleBtnAddCountryActionPerformed(evt);
            }
        });

        buttonGroupConception.add(jToggleBtnAddCountryIrregular);
        jToggleBtnAddCountryIrregular.setText("Ajouter pays Irregulier");
        jToggleBtnAddCountryIrregular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleBtnAddCountryIrregularActionPerformed(evt);
            }
        });

        buttonGroupConception.add(jToggleBtnModifyCountry);
        jToggleBtnModifyCountry.setText("Modifier pays");
        jToggleBtnModifyCountry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleBtnModifyCountryActionPerformed(evt);
            }
        });

        buttonGroupConception.add(jToggleBtnAddLink);
        jToggleBtnAddLink.setText("Ajouter liens");
        jToggleBtnAddLink.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleBtnAddLinkActionPerformed(evt);
            }
        });

        buttonGroupConception.add(jToggleBtnModifyLink);
        jToggleBtnModifyLink.setText("Modifier liens");
        jToggleBtnModifyLink.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleBtnModifyLinkActionPerformed(evt);
            }
        });

        jPanelConceptionOptions.setLayout(new java.awt.CardLayout());

        javax.swing.GroupLayout jPanelBlankLayout = new javax.swing.GroupLayout(jPanelBlank);
        jPanelBlank.setLayout(jPanelBlankLayout);
        jPanelBlankLayout.setHorizontalGroup(
            jPanelBlankLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 362, Short.MAX_VALUE)
        );
        jPanelBlankLayout.setVerticalGroup(
            jPanelBlankLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 420, Short.MAX_VALUE)
        );

        jPanelConceptionOptions.add(jPanelBlank, "card6");

        jLabelAddCountryName.setText("Nom:");

        jLabelAddCountryPop.setText("Population:");

        jTextFieldAddCountryName.setText("Nom");

        jTextFieldAddCountryPop.setText("10000");
        jTextFieldAddCountryPop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldAddCountryPopActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelAddCountryLayout = new javax.swing.GroupLayout(jPanelAddCountry);
        jPanelAddCountry.setLayout(jPanelAddCountryLayout);
        jPanelAddCountryLayout.setHorizontalGroup(
            jPanelAddCountryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAddCountryLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelAddCountryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelAddCountryLayout.createSequentialGroup()
                        .addComponent(jLabelAddCountryName)
                        .addGap(52, 52, 52)
                        .addComponent(jTextFieldAddCountryName, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE))
                    .addGroup(jPanelAddCountryLayout.createSequentialGroup()
                        .addComponent(jLabelAddCountryPop)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldAddCountryPop, javax.swing.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanelAddCountryLayout.setVerticalGroup(
            jPanelAddCountryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAddCountryLayout.createSequentialGroup()
                .addGap(99, 99, 99)
                .addGroup(jPanelAddCountryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelAddCountryName)
                    .addComponent(jTextFieldAddCountryName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelAddCountryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelAddCountryPop)
                    .addComponent(jTextFieldAddCountryPop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(270, Short.MAX_VALUE))
        );

        jPanelConceptionOptions.add(jPanelAddCountry, "card4");

        jButtonDeleteCountry.setText("Supprimer le pays");
        jButtonDeleteCountry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteCountryActionPerformed(evt);
            }
        });

        jLabelModCountryName.setText("Nom:");

        jLabelModCountryPop.setText("Population:");

        jTextFieldChangeCountryName.setText("Nom");
        jTextFieldChangeCountryName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldChangeCountryNameKeyPressed(evt);
            }
        });

        jTextFieldChangeCountryPop.setText("10000");
        jTextFieldChangeCountryPop.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldChangeCountryPopKeyPressed(evt);
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
                    .addComponent(jScrollPaneRegionsList, javax.swing.GroupLayout.DEFAULT_SIZE, 332, Short.MAX_VALUE)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelTitleRegionList)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPaneRegionsList, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonRemoveRegion)
                .addContainerGap(40, Short.MAX_VALUE))
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

        javax.swing.GroupLayout jPanelModifyCountryLayout = new javax.swing.GroupLayout(jPanelModifyCountry);
        jPanelModifyCountry.setLayout(jPanelModifyCountryLayout);
        jPanelModifyCountryLayout.setHorizontalGroup(
            jPanelModifyCountryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelModifyCountryLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelModifyCountryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelRegionOptions, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanelModifyCountryLayout.createSequentialGroup()
                        .addGroup(jPanelModifyCountryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelModCountryPop)
                            .addComponent(jLabelModCountryName))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelModifyCountryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldChangeCountryName)
                            .addComponent(jTextFieldChangeCountryPop)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelModifyCountryLayout.createSequentialGroup()
                        .addGroup(jPanelModifyCountryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButtonModifyRegion, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonAddRegion, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonDeleteCountry, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanelModifyCountryLayout.createSequentialGroup()
                                .addGroup(jPanelModifyCountryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabelTitleRegionName))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanelModifyCountryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextFieldPercentageAddRegion, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
                                    .addComponent(jTextFieldRegionName))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator1)))
                        .addContainerGap())))
        );
        jPanelModifyCountryLayout.setVerticalGroup(
            jPanelModifyCountryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelModifyCountryLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelModifyCountryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelModCountryName)
                    .addComponent(jTextFieldChangeCountryName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelModifyCountryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldChangeCountryPop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelModCountryPop))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonDeleteCountry)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelModifyCountryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelModifyCountryLayout.createSequentialGroup()
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelModifyCountryLayout.createSequentialGroup()
                        .addGroup(jPanelModifyCountryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldRegionName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelTitleRegionName))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanelModifyCountryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldPercentageAddRegion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonAddRegion)
                .addGap(1, 1, 1)
                .addComponent(jButtonModifyRegion)
                .addGap(18, 18, 18)
                .addComponent(jPanelRegionOptions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanelConceptionOptions.add(jPanelModifyCountry, "card2");

        jComboBoxAddLink.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Terrestre", "Maritime", "Aérien" }));

        jLabel6.setText("Ajouter un lien");

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

        jLabel10.setText("Type de lien");

        javax.swing.GroupLayout jPanelAddLinkLayout = new javax.swing.GroupLayout(jPanelAddLink);
        jPanelAddLink.setLayout(jPanelAddLinkLayout);
        jPanelAddLinkLayout.setHorizontalGroup(
            jPanelAddLinkLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAddLinkLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelAddLinkLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanelAddLinkLayout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jComboBoxAddLink, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelAddLinkLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPaneLinks, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE)
                    .addComponent(jButtonDeleteLink, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelAddLinkLayout.setVerticalGroup(
            jPanelAddLinkLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAddLinkLayout.createSequentialGroup()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelAddLinkLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxAddLink, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addGap(33, 33, 33)
                .addComponent(jScrollPaneLinks, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonDeleteLink)
                .addContainerGap(178, Short.MAX_VALUE))
        );

        jPanelConceptionOptions.add(jPanelAddLink, "card5");

        jComboBoxModifyLink.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Terrestre", "Maritime", "Aérien" }));
        jComboBoxModifyLink.setEnabled(false);

        jLabelModifyLinkType.setText("Modifier:");
        jLabelModifyLinkType.setEnabled(false);

        javax.swing.GroupLayout jPanelModifyLinkLayout = new javax.swing.GroupLayout(jPanelModifyLink);
        jPanelModifyLink.setLayout(jPanelModifyLinkLayout);
        jPanelModifyLinkLayout.setHorizontalGroup(
            jPanelModifyLinkLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelModifyLinkLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabelModifyLinkType)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jComboBoxModifyLink, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanelModifyLinkLayout.setVerticalGroup(
            jPanelModifyLinkLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelModifyLinkLayout.createSequentialGroup()
                .addGap(107, 107, 107)
                .addGroup(jPanelModifyLinkLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxModifyLink, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelModifyLinkType))
                .addContainerGap(291, Short.MAX_VALUE))
        );

        jPanelConceptionOptions.add(jPanelModifyLink, "card3");

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
                    .addComponent(jToggleBtnModifyLink, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jToggleBtnAddLink, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jToggleBtnAddCountry, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonResetZoom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jToggleBtnAddCountryIrregular, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(20, Short.MAX_VALUE))
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
                .addComponent(jToggleBtnAddCountry)
                .addGap(6, 6, 6)
                .addComponent(jToggleBtnAddCountryIrregular)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToggleBtnModifyCountry)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToggleBtnAddLink)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToggleBtnModifyLink)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonResetZoom)
                .addContainerGap(510, Short.MAX_VALUE))
            .addGroup(jPanelConceptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelConceptionLayout.createSequentialGroup()
                    .addContainerGap(230, Short.MAX_VALUE)
                    .addComponent(jPanelConceptionOptions, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(57, Short.MAX_VALUE)))
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

        jPanelTransmissionRate.setLayout(new java.awt.GridLayout(1, 0));

        jButtonApplyDisease.setText("Appliquer (maladie)");
        jButtonApplyDisease.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonApplyDiseaseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelDeseaseParamsLayout = new javax.swing.GroupLayout(jPanelDeseaseParams);
        jPanelDeseaseParams.setLayout(jPanelDeseaseParamsLayout);
        jPanelDeseaseParamsLayout.setHorizontalGroup(
            jPanelDeseaseParamsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelMortalityRate, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jPanelReproductionRate, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jPanelCuredRate, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(jPanelDeseaseParamsLayout.createSequentialGroup()
                .addComponent(jButtonApplyDisease, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelTransmissionRate, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanelDeseaseParamsLayout.setVerticalGroup(
            jPanelDeseaseParamsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDeseaseParamsLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jPanelMortalityRate, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jPanelReproductionRate, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jPanelCuredRate, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanelDeseaseParamsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelDeseaseParamsLayout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jPanelTransmissionRate, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelDeseaseParamsLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jButtonApplyDisease, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(376, Short.MAX_VALUE))
        );

        jTabbedPaneSimulationOptions.addTab("Paramètres de la maladie", jPanelDeseaseParams);

        jLabelMesureName.setText("Nom mesure:");

        jTextFieldMesureName.setText("Nom");

        jLabelAdhesionRate.setText("Taux d'adhésion (%):");

        jTextFieldAdhesionRate.setText("80");

        jCheckBoxActiveMesure.setText("Mesure active");

        jScrollPaneOtherMeasures.setViewportView(jListMesures);

        jButtonAddMesure.setText("Ajouter mesure");
        jButtonAddMesure.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddMesureActionPerformed(evt);
            }
        });

        jLabel1.setText("Liens");

        jCheckBox2.setText("Fermé");

        jButtonDeleteMesure.setText("Supprimer mesure");
        jButtonDeleteMesure.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteMesureActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelHealthMesuresLayout = new javax.swing.GroupLayout(jPanelHealthMesures);
        jPanelHealthMesures.setLayout(jPanelHealthMesuresLayout);
        jPanelHealthMesuresLayout.setHorizontalGroup(
            jPanelHealthMesuresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelHealthMesuresLayout.createSequentialGroup()
                .addGroup(jPanelHealthMesuresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonAddMesure, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonDeleteMesure, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPaneOtherMeasures, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
            .addGroup(jPanelHealthMesuresLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelHealthMesuresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelHealthMesuresLayout.createSequentialGroup()
                        .addComponent(jLabelMesureName)
                        .addGap(48, 48, 48)
                        .addComponent(jTextFieldMesureName))
                    .addGroup(jPanelHealthMesuresLayout.createSequentialGroup()
                        .addComponent(jLabelAdhesionRate)
                        .addGap(25, 25, 25)
                        .addComponent(jTextFieldAdhesionRate, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE))
                    .addGroup(jPanelHealthMesuresLayout.createSequentialGroup()
                        .addComponent(jCheckBoxActiveMesure)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanelHealthMesuresLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jCheckBox2)
                        .addGap(16, 16, 16))))
        );
        jPanelHealthMesuresLayout.setVerticalGroup(
            jPanelHealthMesuresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelHealthMesuresLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelHealthMesuresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jCheckBox2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 165, Short.MAX_VALUE)
                .addGroup(jPanelHealthMesuresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldMesureName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelMesureName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelHealthMesuresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelAdhesionRate)
                    .addComponent(jTextFieldAdhesionRate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxActiveMesure)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonAddMesure)
                .addGap(2, 2, 2)
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

        javax.swing.GroupLayout jPanelSimulationLayout = new javax.swing.GroupLayout(jPanelSimulation);
        jPanelSimulation.setLayout(jPanelSimulationLayout);
        jPanelSimulationLayout.setHorizontalGroup(
            jPanelSimulationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelSimulationLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jTabbedPaneSimulationOptions, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                        .addComponent(jButtonForward))
                    .addGroup(jPanelSimulationLayout.createSequentialGroup()
                        .addComponent(jLabelTimeLapse)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextFieldSimulationTimeStep, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jBtnChangeSimulationTimeStep)))
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
                    .addComponent(jButtonForward))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPaneSimulationOptions, javax.swing.GroupLayout.PREFERRED_SIZE, 553, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                        .addComponent(jScrollPaneMap)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTabbedMainPane, javax.swing.GroupLayout.PREFERRED_SIZE, 384, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabelTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 437, Short.MAX_VALUE)
                        .addComponent(jButtonScreenShotWorld)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanelLegend, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanelLegend, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jButtonScreenShotWorld)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTabbedMainPane, javax.swing.GroupLayout.PREFERRED_SIZE, 741, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPaneMap))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItemNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemNewActionPerformed
        worldController.newProjet();
        
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
        regionPts.clear();
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
                    if(regionPts.isEmpty() || select.Id == countrySelected.Id) {
                        SetSelectedCountry(select);
                        regionPts.add(mousePoint);
                        
                        if(regionPts.size() == 2) {                            
                            try {
                                double percentage = Double.parseDouble(jTextFieldPercentageAddRegion.getText()) / 100.0;
                                String name = jTextFieldRegionName.getText();
                                if(percentage > 0.0 && !Utility.StringIsNullOrEmpty(name)) {
                                    worldController.AddRegion(select.Id, regionPts, name, percentage);
                                    UpdateJRegionList(select.Id);

                                    jTextFieldPercentageAddRegion.setBackground(Color.white);
                                    jTextFieldRegionName.setBackground(Color.white);

                                    drawingPanel.repaint();
                                } else {
                                    if(percentage < 0) jTextFieldPercentageAddRegion.setBackground(Color.red);
                                    if(Utility.StringIsNullOrEmpty(name)) jTextFieldRegionName.setBackground(Color.red);
                                }
                            } catch(NumberFormatException e) {
                                    jTextFieldPercentageAddRegion.setBackground(Color.red);
                            }

                            UnselectAddRegion();
                        }
                    } else {
                        UnselectAddRegion();
                    }
                } else {
                    UnselectAddRegion();
                }
                
                break;
            
            case AddCountry:
                countryPts.add(mousePoint);
            
                if(countryPts.size() == 2) { // Un peux bete de faire ca, mais on trouvera une solution
                    try {
                        String name = jTextFieldAddCountryName.getText();
                        int population = Integer.parseInt(jTextFieldAddCountryPop.getText());
                        if(!Utility.StringIsNullOrEmpty(name) && population > 0) {
                            worldController.AddCountry(countryPts, name, population);

                            jTextFieldAddCountryName.setBackground(Color.white);
                            jTextFieldAddCountryPop.setBackground(Color.white);

                            drawingPanel.repaint();
                            countryPts.clear();
                        } else {
                            if(Utility.StringIsNullOrEmpty(name)) jTextFieldAddCountryName.setBackground(Color.red);
                            if(population <= 0) jTextFieldAddCountryPop.setBackground(Color.red);
                        }
                    }catch(NumberFormatException e) {
                        jTextFieldAddCountryPop.setBackground(Color.red);
                    }
                    
                    countryPts.clear();
                }
                break;
            case ModifyCountry:
                SelectCountry(mousePoint);
                if(countrySelected != null) {
                    jTextFieldChangeCountryName.setText(countrySelected.Name);
                    jTextFieldChangeCountryPop.setText(Integer.toString(countrySelected.populationDTO.totalPopulationDTO));
                    UpdateJRegionList(countrySelected.Id);
                }
                break;
            case AddLink:
                List<CountryDTO> countries = worldController.GetCountries();
                CountryDTO selected = Utility.SelectCountry(countries, mousePoint);
                boolean found = false;
                if(selected != null && countrySelected != null) {
                    int linkIndex = jComboBoxAddLink.getSelectedIndex();
                    worldController.AddLink(countrySelected.Id, selected.Id, Link.LinkType.values()[linkIndex]);
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
            CountryDTO country = Utility.SelectCountry(worldController.GetCountries(), evt.getPoint());
            if(country != null) {
                country.SetPosition(evt.getPoint());
                worldController.UpdateCountry(country);
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
                double adhesion = Double.parseDouble(jTextFieldAdhesionRate.getText());
                if(!Utility.StringIsNullOrEmpty(mesureName) && adhesion >= 0) { 
                    worldController.AddMesure(countrySelected.Id, adhesion, jCheckBoxActiveMesure.isSelected(), mesureName);
                    UpdateJMesureList(countrySelected.Id);

                    jTextFieldMesureName.setBackground(Color.white);
                    jTextFieldAdhesionRate.setBackground(Color.white);
                    drawingPanel.repaint();
                } else {
                    if (Utility.StringIsNullOrEmpty(mesureName)) jTextFieldMesureName.setBackground(Color.red);
                    if (adhesion < 0) jTextFieldAdhesionRate.setBackground(Color.red);
                }
            } catch(NumberFormatException e) {
                jTextFieldAdhesionRate.setBackground(Color.red);
            }
        }
    }//GEN-LAST:event_jButtonAddMesureActionPerformed

    private void jButtonApplyDiseaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonApplyDiseaseActionPerformed
        try {
            double infectionRate = Double.parseDouble(jTextFieldReproductionRate.getText())/100;
            double mortalityRate = Double.parseDouble(jTextFieldMortalityRate.getText())/100;
            double cureRate = Double.parseDouble(jTextFieldCuredRate.getText())/100;
            if(infectionRate >= 0 && infectionRate <=1 && mortalityRate >=0 && mortalityRate <=1 && 
                cureRate >= 0 && cureRate <=1) {
                    worldController.UpdateDiseaseFromDTO(infectionRate, mortalityRate, cureRate);
                    jTextFieldReproductionRate.setBackground(Color.white);
                    jTextFieldMortalityRate.setBackground(Color.white);
                    jTextFieldCuredRate.setBackground(Color.white);
                }
            else{
                if(infectionRate < 0) jTextFieldReproductionRate.setBackground(Color.red);
                if(mortalityRate < 0) jTextFieldMortalityRate.setBackground(Color.red);
                if(cureRate < 0) jTextFieldCuredRate.setBackground(Color.red);
            }
        } catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Un des champs n'est pas valide");
        }
    }//GEN-LAST:event_jButtonApplyDiseaseActionPerformed

    private void jTextFieldMortalityRateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldMortalityRateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldMortalityRateActionPerformed

    private void jButtonForwardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonForwardActionPerformed
        worldController.Redo();
    }//GEN-LAST:event_jButtonForwardActionPerformed

    private void jButtonBacktrackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBacktrackActionPerformed
        worldController.Undo();
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
    }//GEN-LAST:event_jBtnPauseActionPerformed

    private void jBtnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnResetActionPerformed
        worldController.resetSimulation();
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

    private void jTextFieldChangeCountryPopKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldChangeCountryPopKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            countrySelected.populationDTO.totalPopulationDTO = Integer.parseInt(jTextFieldChangeCountryPop.getText());
            worldController.UpdateCountry(countrySelected);
            drawingPanel.revalidate();
            drawingPanel.repaint();
        }
    }//GEN-LAST:event_jTextFieldChangeCountryPopKeyPressed

    private void jTextFieldChangeCountryNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldChangeCountryNameKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            countrySelected.Name = jTextFieldChangeCountryName.getText();
            worldController.UpdateCountry(countrySelected);
            drawingPanel.revalidate();
            drawingPanel.repaint();
        }
    }//GEN-LAST:event_jTextFieldChangeCountryNameKeyPressed

    private void jButtonDeleteCountryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteCountryActionPerformed
        if(countrySelected != null) {
            worldController.RemoveCountry(countrySelected.Id);
        }
    }//GEN-LAST:event_jButtonDeleteCountryActionPerformed

    private void jTextFieldAddCountryPopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldAddCountryPopActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldAddCountryPopActionPerformed

    private void jToggleBtnModifyLinkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleBtnModifyLinkActionPerformed
        if (jToggleBtnModifyLink.isSelected()){
            mode = Mode.ModifyLink;
            SetSelectedCountry(null);
            jPanelModifyCountry.setVisible(false);
            jPanelAddCountry.setVisible(false);
            jPanelModifyLink.setVisible(true);
            jPanelAddLink.setVisible(false);
            jPanelBlank.setVisible(false);
        }
    }//GEN-LAST:event_jToggleBtnModifyLinkActionPerformed

    private void jToggleBtnAddLinkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleBtnAddLinkActionPerformed
        if (jToggleBtnAddLink.isSelected()){
            mode = Mode.AddLink;
            SetSelectedCountry(null);
            jPanelModifyCountry.setVisible(false);
            jPanelAddCountry.setVisible(false);
            jPanelModifyLink.setVisible(false);
            jPanelAddLink.setVisible(true);
            jPanelBlank.setVisible(false);
        }
    }//GEN-LAST:event_jToggleBtnAddLinkActionPerformed

    private void jToggleBtnModifyCountryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleBtnModifyCountryActionPerformed
        // TODO add your handling code here:
        if (jToggleBtnModifyCountry.isSelected()){
            mode = Mode.ModifyCountry;
            SetSelectedCountry(null);
            jPanelModifyCountry.setVisible(true);
            jPanelAddCountry.setVisible(false);
            jPanelModifyLink.setVisible(false);
            jPanelAddLink.setVisible(false);
            jPanelBlank.setVisible(false);
        }
    }//GEN-LAST:event_jToggleBtnModifyCountryActionPerformed

    private void jToggleBtnAddCountryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleBtnAddCountryActionPerformed
        // TODO add your handling code here:
        if (jToggleBtnAddCountry.isSelected()){
            mode = Mode.AddCountry;
            SetSelectedCountry(null);
            jPanelAddCountry.setVisible(true);
            jPanelModifyCountry.setVisible(false);
            jPanelModifyLink.setVisible(false);
            jPanelAddLink.setVisible(false);
            jPanelBlank.setVisible(false);
        }
    }//GEN-LAST:event_jToggleBtnAddCountryActionPerformed

    private void jToggleBtnAddCountryIrregularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleBtnAddCountryIrregularActionPerformed
        // TODO add your handling code here:
        if (jToggleBtnAddCountryIrregular.isSelected()){
            //mode = Mode.AddCountry;
            //SetSelectedCountry(null);
            jPanelAddCountry.setVisible(true);
            jPanelModifyCountry.setVisible(false);
            jPanelModifyLink.setVisible(false);
            jPanelAddLink.setVisible(false);
            jPanelBlank.setVisible(false);
        }
    }//GEN-LAST:event_jToggleBtnAddCountryIrregularActionPerformed

    private void jButtonResetZoomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonResetZoomActionPerformed
        drawingPanel.ResetZoom();
    }//GEN-LAST:event_jButtonResetZoomActionPerformed

    private void jButtonScreenShotWorldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScreenShotWorldActionPerformed
        // TODO add your handling code here:
        drawingPanel.SaveScreenShot("test_now_2.png");
       
    }//GEN-LAST:event_jButtonScreenShotWorldActionPerformed

    public void Draw(Graphics2D g2d){
        worldController.Draw(g2d); 
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
                new MainWindow().setVisible(true);
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
    private javax.swing.JButton jButtonAddMesure;
    private javax.swing.JButton jButtonAddRegion;
    private javax.swing.JButton jButtonApplyDisease;
    private javax.swing.JButton jButtonBacktrack;
    private javax.swing.JButton jButtonDeleteCountry;
    private javax.swing.JButton jButtonDeleteLink;
    private javax.swing.JButton jButtonDeleteMesure;
    private javax.swing.JButton jButtonForward;
    private javax.swing.JButton jButtonModifyRegion;
    private javax.swing.JButton jButtonRemoveRegion;
    private javax.swing.JButton jButtonResetZoom;
    private javax.swing.JButton jButtonScreenShotWorld;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBoxActiveMesure;
    private javax.swing.JComboBox<String> jComboBoxAddLink;
    private javax.swing.JComboBox<String> jComboBoxModifyLink;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelAddCountryName;
    private javax.swing.JLabel jLabelAddCountryPop;
    private javax.swing.JLabel jLabelAdhesionRate;
    private javax.swing.JLabel jLabelCase;
    private javax.swing.JLabel jLabelCured;
    private javax.swing.JLabel jLabelDayElapsed;
    private javax.swing.JLabel jLabelDead;
    private javax.swing.JLabel jLabelMesureName;
    private javax.swing.JLabel jLabelModCountryName;
    private javax.swing.JLabel jLabelModCountryPop;
    private javax.swing.JLabel jLabelModifyLinkType;
    private javax.swing.JLabel jLabelPopMondial;
    private javax.swing.JLabel jLabelTimeLapse;
    private javax.swing.JLabel jLabelTitle;
    private javax.swing.JLabel jLabelTitleCase;
    private javax.swing.JLabel jLabelTitleCured;
    private javax.swing.JLabel jLabelTitleCuredRate;
    private javax.swing.JLabel jLabelTitleDayElapsed;
    private javax.swing.JLabel jLabelTitleDead;
    private javax.swing.JLabel jLabelTitleMortalityRate;
    private javax.swing.JLabel jLabelTitlePopMondial;
    private javax.swing.JLabel jLabelTitleRegionList;
    private javax.swing.JLabel jLabelTitleRegionName;
    private javax.swing.JLabel jLabelTitleReproductionRate;
    private javax.swing.JList<String> jListLinks;
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
    private javax.swing.JPanel jPanelAddCountry;
    private javax.swing.JPanel jPanelAddLink;
    private javax.swing.JPanel jPanelBlank;
    private javax.swing.JPanel jPanelConception;
    private javax.swing.JPanel jPanelConceptionOptions;
    private javax.swing.JPanel jPanelCuredRate;
    private javax.swing.JPanel jPanelDeseaseParams;
    private javax.swing.JPanel jPanelDraw;
    private javax.swing.JPanel jPanelHealthMesures;
    private javax.swing.JPanel jPanelLegend;
    private javax.swing.JPanel jPanelModifyCountry;
    private javax.swing.JPanel jPanelModifyLink;
    private javax.swing.JPanel jPanelMortalityRate;
    private javax.swing.JPanel jPanelRegionOptions;
    private javax.swing.JPanel jPanelReproductionRate;
    private javax.swing.JPanel jPanelSimulation;
    private javax.swing.JPanel jPanelTransmissionRate;
    private javax.swing.JScrollPane jScrollPaneLinks;
    private javax.swing.JScrollPane jScrollPaneMap;
    private javax.swing.JScrollPane jScrollPaneOtherMeasures;
    private javax.swing.JScrollPane jScrollPaneRegionsList;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTabbedPane jTabbedMainPane;
    private javax.swing.JTabbedPane jTabbedPaneSimulationOptions;
    private javax.swing.JTextField jTextFieldAddCountryName;
    private javax.swing.JTextField jTextFieldAddCountryPop;
    private javax.swing.JTextField jTextFieldAdhesionRate;
    private javax.swing.JTextField jTextFieldChangeCountryName;
    private javax.swing.JTextField jTextFieldChangeCountryPop;
    private javax.swing.JTextField jTextFieldCuredRate;
    private javax.swing.JTextField jTextFieldMesureName;
    private javax.swing.JTextField jTextFieldMortalityRate;
    private javax.swing.JTextField jTextFieldPercentageAddRegion;
    private javax.swing.JTextField jTextFieldRegionName;
    private javax.swing.JTextField jTextFieldReproductionRate;
    private javax.swing.JTextField jTextFieldSimulationTimeStep;
    private javax.swing.JToggleButton jToggleBtnAddCountry;
    private javax.swing.JToggleButton jToggleBtnAddCountryIrregular;
    private javax.swing.JToggleButton jToggleBtnAddLink;
    private javax.swing.JToggleButton jToggleBtnModifyCountry;
    private javax.swing.JToggleButton jToggleBtnModifyLink;
    // End of variables declaration//GEN-END:variables
}
