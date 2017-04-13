package com.vaadin.pekka.resizablecsslayout.demo;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.HasValue;
import com.vaadin.pekka.resizablecsslayout.ResizableCssLayout;
import com.vaadin.pekka.resizablecsslayout.client.ResizeLocation;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import javax.servlet.annotation.WebServlet;
import java.util.ArrayList;

/**
 * Created by ptandon on 4/13/17.
 */
@Theme("mytheme")
public class DemoUI extends UI {

    private ResizableCssLayout gridWrapper;
    private ResizableCssLayout formWrapper;

    private CheckBox cancelResizeToggle;
    private CheckBox listenerToggle;
    private ResizableCssLayout imageWrapper;

    public enum AvailableResizeLocations {
        All, Corners, Sides, Top_Left, Top, Top_Right, Right, Bottom_Right, Bottom, Bottom_Left, Left;
    }

    @Override
    protected void init(VaadinRequest request) {
        final Grid<GridExampleBean> grid = createGrid();
        gridWrapper = new ResizableCssLayout();
        gridWrapper.setResizable(true);
        gridWrapper.setHeight("400px");
        gridWrapper.setWidth("400px");
        gridWrapper.addComponent(grid);
        gridWrapper.setCaption("Resize from grid's edges");

        Layout form = createForm(grid);
        formWrapper = new ResizableCssLayout(form);
        formWrapper.setResizable(true);
        formWrapper.setCaption("Resize form");
        formWrapper.setHeight("250px");
        formWrapper.setWidth("250px");

        Image image = new Image(null, new ThemeResource("img/swan.jpg"));
        image.setSizeFull();
        imageWrapper = new ResizableCssLayout(image);
        imageWrapper.setResizable(true);
        imageWrapper.setKeepAspectRatio(true);
        imageWrapper.setCaption("Image keeps aspect ratio");
        imageWrapper.setWidth("250px");
        imageWrapper.setHeight("167px");

        final AbsoluteLayout absoluteLayout = new AbsoluteLayout();
        absoluteLayout.setSizeFull();
        absoluteLayout.addComponent(gridWrapper, "top:50px; left:50px;");
        absoluteLayout.addComponent(formWrapper, "right:100px; bottom:100px;");
        absoluteLayout.addComponent(imageWrapper, "top:50px; left:500px");

        HorizontalLayout options = createOptions();

        final VerticalLayout layout = new VerticalLayout();
        layout.addComponent(options);
        layout.addComponent(absoluteLayout);
        layout.setComponentAlignment(absoluteLayout, Alignment.MIDDLE_LEFT);
        layout.setExpandRatio(absoluteLayout, 1.0F);
        layout.setStyleName("demoContentLayout");
        layout.setSizeFull();
        layout.setSpacing(true);
        setContent(layout);

    }

    private Layout createForm(final Grid<GridExampleBean> grid) {
        final VerticalLayout formLayout = new VerticalLayout();
        formLayout.setSizeFull();
//        grid.addSelectionListener(new SelectionListener<GridExampleBean>() {
//            @Override
//            public void selectionChange(SelectionEvent<GridExampleBean> event) {
//
//                formLayout.removeAllComponents();
//                Optional<GridExampleBean> selected = event.getFirstSelectedItem();
//                selected.ifPresent(beanItem -> {
//                    FieldGroup fieldGroup = new FieldGroup(beanItem);
//                    fieldGroup.setBuffered(false);
//                    for (Object propertyId : beanItem.getItemPropertyIds()) {
//                        formLayout.addComponent(fieldGroup
//                                .buildAndBind(propertyId));
//                    }
//                    for (Component component : formLayout) {
//                        formLayout.setComponentAlignment(component,
//                                Alignment.MIDDLE_CENTER);
//                    }
//                });
//            }
//        });
        grid.select(container.get(0));
        return formLayout;
    }

    private HorizontalLayout createOptions() {
        final CheckBox autoAcceptResize = new CheckBox("Auto accept resize",
                gridWrapper.isAutoAcceptResize());

        autoAcceptResize.addValueChangeListener(new HasValue.ValueChangeListener<Boolean>() {
            public void valueChange(HasValue.ValueChangeEvent<Boolean> event) {
                gridWrapper.setAutoAcceptResize(autoAcceptResize.getValue());
                formWrapper.setAutoAcceptResize(autoAcceptResize.getValue());
                imageWrapper.setAutoAcceptResize(autoAcceptResize.getValue());
                cancelResizeToggle.setEnabled(!autoAcceptResize.getValue());
            }
        });

        cancelResizeToggle = new CheckBox("Cancel resize on server");
        cancelResizeToggle.setEnabled(!gridWrapper.isAutoAcceptResize());
        cancelResizeToggle.addValueChangeListener(new HasValue.ValueChangeListener<Boolean>() {
            public void valueChange(HasValue.ValueChangeEvent<Boolean> event) {
                if (cancelResizeToggle.getValue()) {
                    listenerToggle.setValue(true);
                }

            }
        });

        final CheckBox toggleResizable = new CheckBox("Toggle resizable");
        toggleResizable.setValue(true);
        toggleResizable.addValueChangeListener(new HasValue.ValueChangeListener<Boolean>() {
            public void valueChange(HasValue.ValueChangeEvent<Boolean> event) {
                gridWrapper.setResizable(toggleResizable.getValue());
                formWrapper.setResizable(toggleResizable.getValue());
                imageWrapper.setResizable(toggleResizable.getValue());
            }
        });

        final CheckBox aspectRatioToggle = new CheckBox("Keep Aspect Ratio");
        aspectRatioToggle.addValueChangeListener(new HasValue.ValueChangeListener<Boolean>() {
            public void valueChange(HasValue.ValueChangeEvent<Boolean> event) {
                gridWrapper.setKeepAspectRatio(aspectRatioToggle.getValue());
                formWrapper.setKeepAspectRatio(aspectRatioToggle.getValue());
            }
        });

        listenerToggle = new CheckBox("Server side listener");
        listenerToggle.addValueChangeListener(new HasValue.ValueChangeListener<Boolean>() {
            ResizableCssLayout.ResizeListener listener = new ResizableCssLayout.ResizeListener() {

                public void resizeStart(ResizableCssLayout.ResizeStartEvent event) {
                    Notification.show("Resize Started",
                            "Location: " + event.getResizeLocation(),
                            Notification.Type.TRAY_NOTIFICATION);
                }

                public void resizeEnd(ResizableCssLayout.ResizeEndEvent event) {
                    if (!gridWrapper.isAutoAcceptResize()
                            && cancelResizeToggle.getValue()) {
                        Notification.show("Resize Ended - Canceled",
                                "Width / Height: " + event.getWidth() + "/"
                                        + event.getHeight(),
                                Notification.Type.TRAY_NOTIFICATION);
                        gridWrapper.cancelResize();
                        formWrapper.cancelResize();
                        gridWrapper.cancelResize();
                    } else {
                        Notification.show("Resize Ended", "Width / Height: "
                                        + event.getWidth() + "/" + event.getHeight(),
                                Notification.Type.TRAY_NOTIFICATION);
                    }
                }

                public void resizeCancel(ResizableCssLayout.ResizeCancelEvent event) {
                    Notification.show("Resize Canceled",
                            Notification.Type.TRAY_NOTIFICATION);
                }
            };

            public void valueChange(HasValue.ValueChangeEvent<Boolean> event) {
                if (listenerToggle.getValue()) {
                    gridWrapper.addResizeListener(listener);
                    formWrapper.addResizeListener(listener);
                    imageWrapper.addResizeListener(listener);
                } else {
                    gridWrapper.removeResizeListener(listener);
                    formWrapper.removeResizeListener(listener);
                    imageWrapper.removeResizeListener(listener);
                }
            }
        });
        listenerToggle.setValue(true);

        final HorizontalLayout options = new HorizontalLayout();
        options.addComponent(toggleResizable);
        options.addComponent(listenerToggle);
        options.addComponent(autoAcceptResize);
        options.addComponent(cancelResizeToggle);
        options.addComponent(createResizeLocations());
        options.addComponent(aspectRatioToggle);
        for (Component component : options) {
            options.setComponentAlignment(component, Alignment.MIDDLE_CENTER);
        }
        options.setWidth(null);
        options.setSpacing(true);
        return options;
    }

    private ComboBox createResizeLocations() {
        final ComboBox<AvailableResizeLocations> comboBox =
                new ComboBox<AvailableResizeLocations>("Available Resize Locations:");
        comboBox.addStyleName(ValoTheme.COMBOBOX_TINY);
        comboBox.setItems(AvailableResizeLocations.values());
        comboBox.setSelectedItem(AvailableResizeLocations.All);
        comboBox.addValueChangeListener(new HasValue.ValueChangeListener<AvailableResizeLocations>() {

            public void valueChange(HasValue.ValueChangeEvent<AvailableResizeLocations> event) {
                AvailableResizeLocations value = event.getValue();
                final ResizeLocation resizeLocation = ResizeLocation.valueOf(value.toString().toUpperCase());
                switch (value) {
                    case All:
                        gridWrapper.setAllLocationsResizable();
                        formWrapper.setAllLocationsResizable();
                        imageWrapper.setAllLocationsResizable();
                        break;
                    case Corners:
                        gridWrapper.setCornersResizable();
                        formWrapper.setCornersResizable();
                        imageWrapper.setCornersResizable();
                        break;
                    case Sides:
                        gridWrapper.setSidesResizable();
                        formWrapper.setSidesResizable();
                        imageWrapper.setCornersResizable();
                        break;
                    default:
                        gridWrapper.setResizeLocations(resizeLocation);
                        formWrapper.setResizeLocations(resizeLocation);
                        imageWrapper.setResizeLocations(resizeLocation);
                        break;
                }
            }
        });
        return comboBox;
    }

    ArrayList<GridExampleBean> container = new ArrayList<GridExampleBean>();

    private Grid<GridExampleBean> createGrid() {
        for (int i = 0; i < 1000; i++) {
            container.add(new GridExampleBean("Bean " + i, i * i, i / 10d));
        }
        Grid<GridExampleBean> grid = new Grid<GridExampleBean>(GridExampleBean.class);
        grid.setItems(container);
        grid.setSizeFull();
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        return grid;
    }

    public class GridExampleBean {
        private String name;
        private int count;
        private double amount;

        public GridExampleBean() {
        }

        public GridExampleBean(String name, int count, double amount) {
            this.name = name;
            this.count = count;
            this.amount = amount;
        }

        public String getName() {
            return name;
        }

        public int getCount() {
            return count;
        }

        public double getAmount() {
            return amount;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public double getSum() {
            return getAmount() * getCount();
        }
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = DemoUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}