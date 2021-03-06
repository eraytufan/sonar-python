/*
 * SonarQube Python Plugin
 * Copyright (C) 2011-2019 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.python.checks;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.AstNodeType;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.sonar.check.Rule;
import org.sonar.python.PythonCheck;
import org.sonar.python.api.PythonGrammar;
import org.sonar.python.api.PythonPunctuator;

@Rule(key = PreIncrementDecrementCheck.CHECK_KEY)
public class PreIncrementDecrementCheck extends PythonCheck {
  public static final String CHECK_KEY = "PreIncrementDecrement";
  private static final String MESSAGE = "This statement doesn't produce the expected result, replace use of non-existent pre-%srement operator";

  @Override
  public Set<AstNodeType> subscribedKinds() {
    return Collections.singleton(PythonGrammar.FACTOR);
  }

  @Override
  public void visitNode(AstNode astNode) {
    List<AstNode> children = astNode.getChildren();
    AstNode firstChild = children.get(0);
    AstNode secondChild = children.get(1);
    if (firstChild.is(PythonPunctuator.PLUS) && secondChild.getFirstChild().is(PythonPunctuator.PLUS)) {
      addIssue(astNode, String.format(MESSAGE, "inc"));
    }
    if (firstChild.is(PythonPunctuator.MINUS) && secondChild.getFirstChild().is(PythonPunctuator.MINUS)) {
      addIssue(astNode, String.format(MESSAGE, "dec"));
    }
  }

}
