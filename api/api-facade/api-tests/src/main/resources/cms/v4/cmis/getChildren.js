/*
 * Copyright (c) 2010-2020 SAP and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *   SAP - initial API and implementation
 */
var cmis = require('cms/v4/cmis');

var session = cmis.getSession();

var rootFolder = session.getRootFolder();

var result = rootFolder.getChildren();

result !== null && result !== undefined;
