/*
 * This project is licensed under the MIT license. Module model-view-viewmodel is using ZK framework licensed under LGPL (see lgpl-3.0.txt).
 *
 * The MIT License
 * Copyright © 2014-2022 Ilkka Seppälä
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.iluwatar.bytecode;

import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of virtual machine.
 */
@Getter
@Slf4j
public class VirtualMachine {

  private final Stack<Integer> stack = new Stack<>();

  private final Wizard[] wizards = new Wizard[2];

  /**
   * No-args constructor.
   */
  public VirtualMachine() {
    wizards[0] = new Wizard(randomInt(3, 32), randomInt(3, 32), randomInt(3, 32),
        0, 0);
    wizards[1] = new Wizard(randomInt(3, 32), randomInt(3, 32), randomInt(3, 32),
        0, 0);
  }

  /**
   * Constructor taking the wizards as arguments.
   */
  public VirtualMachine(Wizard wizard1, Wizard wizard2) {
    wizards[0] = wizard1;
    wizards[1] = wizard2;
  }

  /**
   * Executes provided bytecode.
   *
   * @param bytecode to execute
   */
  public void execute(int[] bytecode) {
    int i = 0;
    while (i < bytecode.length) {
      int instructionValue = bytecode[i];
      Instruction instruction = Instruction.getInstruction(instructionValue);

      switch (instruction) {
        case LITERAL:
          int value = bytecode[++i];
          stack.push(value);
          break;
        case SET_AGILITY:
        case SET_WISDOM:
        case SET_HEALTH:
          handleSetAttribute(instruction, stack.pop(), stack.pop());
          break;
        case GET_HEALTH:
        case GET_AGILITY:
        case GET_WISDOM:
          handleGetAttribute(instruction, stack.pop());
          break;
        case ADD:
        case DIVIDE:
          handleMathOperation(instruction, stack.pop(), stack.pop());
          break;
        case PLAY_SOUND:
        case SPAWN_PARTICLES:
          handleWizardAction(instruction, stack.pop());
          break;
        default:
          throw new IllegalArgumentException("Invalid instruction value");
      }

      LOGGER.info("Executed " + instruction.name() + ", Stack contains " + getStack());
      i++;
    }
  }

  private void handleSetAttribute(Instruction instruction, int amount, int wizard) {
    switch (instruction) {
      case SET_HEALTH:
        wizards[wizard].setHealth(amount);
        break;
      case SET_WISDOM:
        wizards[wizard].setWisdom(amount);
        break;
      case SET_AGILITY:
        wizards[wizard].setAgility(amount);
        break;
    }
  }

  private void handleGetAttribute(Instruction instruction, int wizard) {
    switch (instruction) {
      case GET_HEALTH:
        stack.push(wizards[wizard].getHealth());
        break;
      case GET_WISDOM:
        stack.push(wizards[wizard].getWisdom());
        break;
      case GET_AGILITY:
        stack.push(wizards[wizard].getAgility());
        break;
    }
  }

  private void handleMathOperation(Instruction instruction, int a, int b) {
    switch (instruction) {
      case ADD:
        stack.push(a + b);
        break;
      case DIVIDE:
        stack.push(b / a);
        break;
    }
  }

  private void handleWizardAction(Instruction instruction, int wizard) {
    switch (instruction) {
      case PLAY_SOUND:
        wizards[wizard].playSound();
        break;
      case SPAWN_PARTICLES:
        wizards[wizard].spawnParticles();
        break;
    }
  }


  public void setHealth(int wizard, int amount) {
    wizards[wizard].setHealth(amount);
  }

  public void setWisdom(int wizard, int amount) {
    wizards[wizard].setWisdom(amount);
  }

  public void setAgility(int wizard, int amount) {
    wizards[wizard].setAgility(amount);
  }

  public int getHealth(int wizard) {
    return wizards[wizard].getHealth();
  }

  public int getWisdom(int wizard) {
    return wizards[wizard].getWisdom();
  }

  public int getAgility(int wizard) {
    return wizards[wizard].getAgility();
  }

  private int randomInt(int min, int max) {
    return ThreadLocalRandom.current().nextInt(min, max + 1);
  }
}
