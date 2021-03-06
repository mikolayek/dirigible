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
var digest = require('utils/v4/digest');

var input = 'ABC';
var result = digest.md5Hex(input);

console.log(result);

result === '902fbdd2b1df0c4f70b4a5d23525e932';
