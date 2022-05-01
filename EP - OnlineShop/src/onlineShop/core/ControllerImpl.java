package onlineShop.core;

import onlineShop.common.constants.ExceptionMessages;
import onlineShop.common.constants.OutputMessages;
import onlineShop.core.interfaces.Controller;
import onlineShop.models.products.components.*;
import onlineShop.models.products.computers.Computer;
import onlineShop.models.products.computers.DesktopComputer;
import onlineShop.models.products.computers.Laptop;
import onlineShop.models.products.peripherals.*;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ControllerImpl implements Controller {

    private Map<Integer, Computer> computerMap;
    private Map<Integer, Component> componentMap;
    private Map<Integer, Peripheral> peripheralMap;


    public ControllerImpl() {
        this.computerMap = new LinkedHashMap<>();
        this.componentMap = new LinkedHashMap<>();
        this.peripheralMap = new LinkedHashMap<>();
    }

    @Override  // !!!
    public String addComputer(String computerType, int id, String manufacturer, String model, double price) {
        if(computerMap.containsKey(id)){
            throw new IllegalArgumentException(ExceptionMessages.EXISTING_COMPUTER_ID);
        }
        Computer computer;
        if(computerType.equals("DesktopComputer")){
            computer = new DesktopComputer(id, manufacturer, model, price);
        } else if(computerType.equals("Laptop")){
            computer = new Laptop(id, manufacturer, model, price);
        } else {
            throw new IllegalArgumentException(ExceptionMessages.INVALID_COMPUTER_TYPE);
        }
        this.computerMap.put(id,computer);
        return String.format(OutputMessages.ADDED_COMPUTER,id);
    }

    @Override
    public String addPeripheral(int computerId, int id, String peripheralType, String manufacturer, String model, double price, double overallPerformance, String connectionType) {
        if(!computerMap.containsKey(computerId)){
            throw new IllegalArgumentException(ExceptionMessages.NOT_EXISTING_COMPUTER_ID);
        }
        if(peripheralMap.containsKey(id)){
            throw new IllegalArgumentException(ExceptionMessages.EXISTING_PERIPHERAL_ID);
        }
        Peripheral peripheral = null;
        if(peripheralType.equals("Headset")){
            peripheral = new Headset(id, manufacturer, model, price, overallPerformance, connectionType);
        } else if(peripheralType.equals("Keyboard")){
            peripheral = new Keyboard(id, manufacturer, model, price, overallPerformance, connectionType);
        } else if(peripheralType.equals("Monitor")){
            peripheral = new Monitor(id, manufacturer, model, price, overallPerformance, connectionType);
        } else if(peripheralType.equals("Mouse")){
            peripheral = new Mouse(id, manufacturer, model, price, overallPerformance, connectionType);
        } else{
            throw new IllegalArgumentException(ExceptionMessages.INVALID_PERIPHERAL_TYPE);
        }

        this.computerMap.get(computerId).addPeripheral(peripheral);
        this.peripheralMap.put(id,peripheral);

        return String.format(OutputMessages.ADDED_PERIPHERAL
                ,peripheralType
                ,id
                ,computerId);
    }

    @Override
    public String removePeripheral(String peripheralType, int computerId) {
        if(!computerMap.containsKey(computerId)){
            throw new IllegalArgumentException(ExceptionMessages.NOT_EXISTING_COMPUTER_ID);
        }
        Peripheral peripheral = computerMap.get(computerId).removePeripheral(peripheralType);
        this.peripheralMap.remove(peripheral.getId());

        return String.format(OutputMessages.REMOVED_PERIPHERAL,peripheralType,peripheral.getId());
    }

    @Override
    public String addComponent(int computerId, int id, String componentType, String manufacturer, String model, double price, double overallPerformance, int generation) {
        if(!computerMap.containsKey(computerId)){
            throw new IllegalArgumentException(ExceptionMessages.NOT_EXISTING_COMPUTER_ID);
        }
        if(componentMap.containsKey(id)){
            throw new IllegalArgumentException(ExceptionMessages.EXISTING_COMPONENT_ID);
        }
        Component component;
        if(componentType.equals("CentralProcessingUnit")){
            component = new CentralProcessingUnit(id, manufacturer, model, price, overallPerformance, generation);
        } else if(componentType.equals("Motherboard")){
            component = new Motherboard(id, manufacturer, model, price, overallPerformance, generation);
        } else if(componentType.equals("PowerSupply")){
            component = new PowerSupply(id, manufacturer, model, price, overallPerformance, generation);
        } else if(componentType.equals("RandomAccessMemory")){
            component = new RandomAccessMemory(id, manufacturer, model, price, overallPerformance, generation);
        } else if(componentType.equals("SolidStateDrive")){
            component = new SolidStateDrive(id, manufacturer, model, price, overallPerformance, generation);
        } else if(componentType.equals("VideoCard")){
            component = new VideoCard(id, manufacturer, model, price, overallPerformance, generation);
        } else {
            throw new IllegalArgumentException(ExceptionMessages.INVALID_COMPONENT_TYPE);
        }

        this.computerMap.get(computerId).addComponent(component);
        this.componentMap.put(id,component);

        return String.format(OutputMessages.ADDED_COMPONENT
                ,componentType
                ,id
                ,computerId);
    }

    @Override
    public String removeComponent(String componentType, int computerId) {
        if(!computerMap.containsKey(computerId)){
            throw new IllegalArgumentException(ExceptionMessages.NOT_EXISTING_COMPUTER_ID);
        }
        Component component = this.computerMap.get(computerId).removeComponent(componentType);
        this.componentMap.remove(component.getId());

        return String.format(OutputMessages.REMOVED_COMPONENT
                ,componentType
                ,component.getId());
    }

    @Override
    public String buyComputer(int id) {
        if(!computerMap.containsKey(id)){
            throw new IllegalArgumentException(ExceptionMessages.NOT_EXISTING_COMPUTER_ID);
        }
        Computer computer = computerMap.get(id);
        return computer.toString();
    }

    @Override // !!!
    public String BuyBestComputer(double budget) {
        List<Computer> filteredComputer =  computerMap.values().stream().filter(c -> c.getPrice() <= budget)
                .sorted(Comparator.comparing(Computer::getOverallPerformance).reversed())
                .collect(Collectors.toList());

        if(filteredComputer.isEmpty()){
            throw new IllegalArgumentException(String.format(ExceptionMessages.CAN_NOT_BUY_COMPUTER,budget));
        }
        Computer computer = filteredComputer.get(0);
        computerMap.remove(computer.getId());

        return computer.toString();
    }

    @Override
    public String getComputerData(int id) {
        if(!computerMap.containsKey(id)){
            throw new IllegalArgumentException(ExceptionMessages.NOT_EXISTING_COMPUTER_ID);
        }
        return computerMap.get(id).toString();
    }


}
